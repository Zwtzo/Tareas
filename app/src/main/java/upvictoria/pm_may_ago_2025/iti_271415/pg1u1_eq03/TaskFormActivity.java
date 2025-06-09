package upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;
import java.util.List;
import upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03.objects.Subject;
import upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03.objects.Task;

public class TaskFormActivity extends AppCompatActivity {

    private EditText titleInput, descriptionInput, dateInput;
    private Spinner statusSpinner;
    private Spinner subjectSpinner;
    private Button saveBtn;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_form);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationOnClickListener(v -> finish());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setTitle("Crear Tarea");

        // EVENTOS DE LOS BOTONES PARA CHECAR QUE TODOS ESTEN LLENOS
        titleInput = findViewById(R.id.titleInput);
        addListener(titleInput);

        descriptionInput = findViewById(R.id.descriptionInput);
        addListener(descriptionInput);

        dateInput = findViewById(R.id.dateInput);

        subjectSpinner = findViewById(R.id.subjectSpinner);
        addListener(subjectSpinner);

        statusSpinner = findViewById(R.id.statusSpinner);
        addListener(statusSpinner);

        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setEnabled(false);

        // Llenar el Spinner con los estados
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        // llenar spinner de asignaturas
        List<Subject> subjects = TaskDatabase.getInstance(this).subjectDao().getAllSubjectsSorted();
        ArrayAdapter<Subject> subjectAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, subjects);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectSpinner.setAdapter(subjectAdapter);

        // Mostrar calendario al hacer clic en el campo de fecha
        Calendar calendar = Calendar.getInstance();
        dateInput.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    TaskFormActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                        dateInput.setText(date);
                        enableSaveButton();
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        // Modo edición (tarea existente)
        if (getIntent().hasExtra("task")) {
            task = (Task) getIntent().getSerializableExtra("task");

            titleInput.setText(task.title);
            descriptionInput.setText(task.description);
            dateInput.setText(task.date);
            statusSpinner.setSelection(adapter.getPosition(task.status));

            titleInput.setEnabled(false);
            descriptionInput.setEnabled(true);
            dateInput.setEnabled(true);
        }

        // Botón guardar
        saveBtn.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String desc = descriptionInput.getText().toString().trim();
            String date = dateInput.getText().toString().trim();
            String subjectName = subjectSpinner.getSelectedItem().toString();

            String status = statusSpinner.getSelectedItem().toString();

            if (task == null) {
                task = new Task(title, desc, date, status, subjectName);
            } else {
                task.status = status; // Solo se actualiza el estado
            }

            TaskDatabase.getInstance(this).taskDao().insertOrUpdate(task);
            finish();
        });
    }

    protected void addListener(EditText editText){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // no se necesita implementación aquí
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // tampoco aquí
            }

            @Override
            public void afterTextChanged(Editable s) {
                enableSaveButton();
            }
        });
    }

    protected void addListener(Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                enableSaveButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                enableSaveButton();
            }
        });
    }

    protected void enableSaveButton() {
        if(subjectSpinner.getSelectedItem() == null) {
            return;
        }

        saveBtn.setEnabled(!titleInput.getText().toString().trim().isEmpty() &&
                !descriptionInput.getText().toString().trim().isEmpty() &&
                !dateInput.getText().toString().trim().isEmpty() &&
                !subjectSpinner.getSelectedItem().toString().isEmpty() &&
                !statusSpinner.getSelectedItem().toString().isEmpty());
    }
}
