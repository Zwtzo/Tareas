package upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private MaterialButton btnTodos;
    private MaterialButton btnPendientes;
    private MaterialButton btnEnProceso;
    private MaterialButton btnCompletados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setTitle("Task Manager");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // botones del filtro
        btnTodos = findViewById(R.id.btnTodos);
        btnTodos.setChecked(true);
        btnPendientes = findViewById(R.id.btnPendientes);
        btnEnProceso = findViewById(R.id.btnProgreso);
        btnCompletados = findViewById(R.id.btnCompletados);

        // Obtener la lista de tareas ordenada por fecha
        taskList = TaskDatabase.getInstance(this).taskDao().getAllTasksSorted();

        taskAdapter = new TaskAdapter(taskList, this);
        recyclerView.setAdapter(taskAdapter);

        findViewById(R.id.fabAddTask).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, TaskFormActivity.class));
        });

        showTodas();

        // ligar acciones
        btnTodos.setOnClickListener(v -> showTodas());
        btnPendientes.setOnClickListener(v -> showPendientes());
        btnEnProceso.setOnClickListener(v -> showEnProgreso());
        btnCompletados.setOnClickListener(v -> showCompletadas());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar la lista cuando se vuelve a la actividad
        taskList.clear();
        taskList.addAll(TaskDatabase.getInstance(this).taskDao().getAllTasksSorted());
        taskAdapter.notifyDataSetChanged();
    }

    public void refreshList() {
        MaterialButtonToggleGroup btnGroup = findViewById(R.id.toggleGroup);
        switch (btnGroup.getCheckedButtonId()){
            case 0:
                showTodas();
                break;
            case 1:
                showPendientes();
                break;
            case 2:
                showEnProgreso();
                break;
            case 3:
                showCompletadas();
                break;
        }
    }

    // BTN ACCIONES
    public void showTodas() {
        taskList.clear();
        taskList.addAll(TaskDatabase.getInstance(this).taskDao().getAllTasksSorted());
        taskAdapter.notifyDataSetChanged();
        showAviso(taskList.size(), "");
    }

    public void showPendientes() {
        taskList.clear();
        taskList.addAll(TaskDatabase.getInstance(this).taskDao().getTasksByStatus("Pendiente"));
        taskAdapter.notifyDataSetChanged();
        showAviso(taskList.size(), "pendientes");
    }

    public void showEnProgreso() {
        taskList.clear();
        taskList.addAll(TaskDatabase.getInstance(this).taskDao().getTasksByStatus("En progreso"));
        taskAdapter.notifyDataSetChanged();
        showAviso(taskList.size(), "en progreso");
    }

    public void showCompletadas() {
        taskList.clear();
        taskList.addAll(TaskDatabase.getInstance(this).taskDao().getTasksByStatus("Completada"));
        taskAdapter.notifyDataSetChanged();
        showAviso(taskList.size(), "completadas");
    }

    public void showAviso(int size, String sufix) {
        TextView aviso = findViewById(R.id.aviso);
        if (size == 0) {
            aviso.setText("No hay tareas " + sufix);
            aviso.setVisibility(TextView.VISIBLE);
        } else {
            aviso.setVisibility(TextView.GONE);
        }
    }

    // [[][][][][][][][][][][][][]]
    // Calendar Functions
    // [[][][][][][][][][][][][][]]

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull android.view.MenuItem item) {
        if (item.getItemId() == R.id.action_calendar) {
            Intent intent = new Intent(this, CalendarActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
