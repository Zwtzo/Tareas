package upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class TaskFormActivity extends AppCompatActivity {

    private EditText titleInput, descriptionInput, dateInput;
    private Spinner statusSpinner;
    private Button saveBtn;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_form);

        titleInput = findViewById(R.id.titleInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        dateInput = findViewById(R.id.dateInput);
        statusSpinner = findViewById(R.id.statusSpinner);
        saveBtn = findViewById(R.id.saveBtn);

        // Llenar el Spinner con los estados
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

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

            // 🔒 Bloquear edición de título, descripción y fecha
            titleInput.setEnabled(false);
            descriptionInput.setEnabled(false);
            dateInput.setEnabled(false);
        }

        // Botón guardar
        saveBtn.setOnClickListener(v -> {
            String title = titleInput.getText().toString().trim();
            String desc = descriptionInput.getText().toString().trim();
            String date = dateInput.getText().toString().trim();
            String status = statusSpinner.getSelectedItem().toString();

            if (task == null) {
                task = new Task(title, desc, date, status);
            } else {
                task.status = status; // Solo se actualiza el estado
            }

            TaskDatabase.getInstance(this).taskDao().insertOrUpdate(task);
            finish();
        });
    }
}
