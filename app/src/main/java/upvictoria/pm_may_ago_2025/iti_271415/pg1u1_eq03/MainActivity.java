package upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import java.util.List;

import upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03.objects.Subject;
import upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03.objects.Task;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private MaterialButton btnTodos;
    private MaterialButton btnPendientes;
    private MaterialButton btnEnProceso;
    private MaterialButton btnCompletados;
    private Spinner subjectSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setTitle("Task Manager");

        // botones del filtro
        btnTodos = findViewById(R.id.btnTodos);
        btnTodos.setChecked(true);
        btnPendientes = findViewById(R.id.btnPendientes);
        btnEnProceso = findViewById(R.id.btnProgreso);
        btnCompletados = findViewById(R.id.btnCompletados);
        subjectSpinner = findViewById(R.id.spinnerMaterias);

        // Obtener la lista de tareas ordenada por fecha
        taskList = TaskDatabase.getInstance(this).taskDao().getAllTasksSorted();
        taskAdapter = new TaskAdapter(taskList, this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);

        // poner materias en el spinner
        Subject todas = new Subject("Todas");
        List<Subject> subjects = TaskDatabase.getInstance(this).subjectDao().getAllSubjectsSorted();
        subjects.add(0, todas); // Agregar "Todas" al inicio de la lista

        ArrayAdapter<Subject> subjectAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, subjects);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(subjectAdapter);

        // accion de cambio del spinner de materias
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSubject = parent.getItemAtPosition(position).toString();
                MaterialButtonToggleGroup toggleGroup = findViewById(R.id.toggleGroup);
                int buttonId = toggleGroup.getCheckedButtonId();

                if (buttonId == R.id.btnTodos) {
                    showTodas();
                } else if (buttonId == R.id.btnPendientes) {
                    showPendientes();
                } else if (buttonId == R.id.btnProgreso) {
                    showEnProgreso();
                } else if (buttonId == R.id.btnCompletados) {
                    showCompletadas();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // acciones de los botones
        findViewById(R.id.fabAddTask).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, TaskFormActivity.class)));

        findViewById(R.id.btnMaterias).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SubjectFormActivity.class)));

        showTodas();

        // ligar acciones
        btnTodos.setOnClickListener(v -> showTodas());
        btnPendientes.setOnClickListener(v -> showPendientes());
        btnEnProceso.setOnClickListener(v -> showEnProgreso());
        btnCompletados.setOnClickListener(v -> showCompletadas());
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        // Recargar la lista cuando se vuelve a la actividad
        refreshList();
        Subject todas = new Subject("Todas");
        List<Subject> subjects = TaskDatabase.getInstance(this).subjectDao().getAllSubjectsSorted();
        subjects.add(0, todas); // Agregar "Todas" al inicio de la lista

        ArrayAdapter<Subject> subjectAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, subjects);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(subjectAdapter);
    }

    public void refreshList() {
        String selectedSubject = subjectSpinner.getSelectedItem().toString();
        if (selectedSubject.equals("Todas")) {
            showTodas();
        } else {
            // Filtrar tareas por materia seleccionada
            taskList.clear();
            taskList.addAll(TaskDatabase.getInstance(MainActivity.this).taskDao().getAllSubjectTasks(selectedSubject));
            taskAdapter.notifyDataSetChanged();
            showAviso(taskList.size(), "");
        }
    }

    // BTN ACCIONES
    @SuppressLint("NotifyDataSetChanged")
    public void showTodas() {
        taskList.clear();

        String selectedSubject = subjectSpinner.getSelectedItem().toString();

        if (selectedSubject.equals("Todas")) {
            taskList.addAll(TaskDatabase.getInstance(this).taskDao().getAllTasksSorted());
        } else {
            taskList.addAll(TaskDatabase.getInstance(this).taskDao().getAllSubjectTasks(selectedSubject));
        }

        taskAdapter.notifyDataSetChanged();
        showAviso(taskList.size(), "");
    }

    @SuppressLint("NotifyDataSetChanged")
    public void showPendientes() {
        taskList.clear();

        String selectedSubject = subjectSpinner.getSelectedItem().toString();

        if (selectedSubject.equals("Todas")) {
            taskList.addAll(TaskDatabase.getInstance(this).taskDao().getAllTasksByStatus("Pendiente"));
        } else {
            taskList.addAll(TaskDatabase.getInstance(this).taskDao().getSubjectTasksByStatus(selectedSubject, "Pendiente"));
        }

        taskAdapter.notifyDataSetChanged();
        showAviso(taskList.size(), "pendientes");
    }

    @SuppressLint("NotifyDataSetChanged")
    public void showEnProgreso() {
        taskList.clear();

        String selectedSubject = subjectSpinner.getSelectedItem().toString();

        if (selectedSubject.equals("Todas")) {
            taskList.addAll(TaskDatabase.getInstance(this).taskDao().getAllTasksByStatus("En progreso"));
        } else {
            taskList.addAll(TaskDatabase.getInstance(this).taskDao().getSubjectTasksByStatus(selectedSubject, "En progreso"));
        }

        taskAdapter.notifyDataSetChanged();
        showAviso(taskList.size(), "en progreso");
    }

    @SuppressLint("NotifyDataSetChanged")
    public void showCompletadas() {
        taskList.clear();

        String selectedSubject = subjectSpinner.getSelectedItem().toString();

        if (selectedSubject.equals("Todas")) {
            taskList.addAll(TaskDatabase.getInstance(this).taskDao().getAllTasksByStatus("Completada"));
        } else {
            taskList.addAll(TaskDatabase.getInstance(this).taskDao().getSubjectTasksByStatus(selectedSubject, "Completada"));
        }
        taskAdapter.notifyDataSetChanged();
        showAviso(taskList.size(), "completadas");
    }

    @SuppressLint("SetTextI18n")
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
