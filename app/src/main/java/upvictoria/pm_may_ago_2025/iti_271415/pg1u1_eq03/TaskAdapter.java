package upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private final List<Task> taskList;
    private final Context context;

    public TaskAdapter(List<Task> taskList, Context context) {
        this.taskList = taskList;
        this.context = context;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.title.setText(task.title);
        holder.date.setText(task.date);
        holder.status.setText(task.status);

        holder.itemView.setOnClickListener(v -> {
            String[] estados = {"Pendiente", "En progreso", "Completada"};

            // Crear diálogo de actualización
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Actualizar estado");

            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_status, null);
            Spinner spinner = dialogView.findViewById(R.id.statusSpinnerDialog);

            // Configurar el adaptador del Spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, estados);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            // Seleccionar el estado actual
            int index = java.util.Arrays.asList(estados).indexOf(task.status);
            if (index >= 0) {
                spinner.setSelection(index);
            }

            builder.setView(dialogView);

            builder.setPositiveButton("Guardar", (dialog, which) -> {
                task.status = spinner.getSelectedItem().toString();
                TaskDatabase.getInstance(context).taskDao().update(task);
                notifyItemChanged(holder.getAdapterPosition());
                ((MainActivity) context).refreshList();
            });

            builder.setNegativeButton("Cancelar", null);
            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, status;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textTitle);
            date = itemView.findViewById(R.id.textDate);
            status = itemView.findViewById(R.id.textStatus);
        }
    }
}
