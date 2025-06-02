package upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtener la lista de tareas ordenada por fecha
        taskList = TaskDatabase.getInstance(this).taskDao().getAllTasksSorted();

        taskAdapter = new TaskAdapter(taskList, this);
        recyclerView.setAdapter(taskAdapter);

        findViewById(R.id.fabAddTask).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, TaskFormActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar la lista cuando se vuelve a la actividad
        taskList.clear();
        taskList.addAll(TaskDatabase.getInstance(this).taskDao().getAllTasksSorted());
        taskAdapter.notifyDataSetChanged();
    }
}
