package upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03.objects.Task;

public class CalendarActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private RecyclerView recyclerView;
    private TextView tasksLabel;
    private TaskAdapter adapter;
    private List<Task> allTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("Calendario");
        toolbar.setNavigationOnClickListener(v -> finish());

        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerTasksForDate);
        tasksLabel = findViewById(R.id.tasksLabel);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        allTasks = TaskDatabase.getInstance(this).taskDao().getAllTasks();
        if (allTasks == null) {
            allTasks = Collections.emptyList();
        }

        Set<CalendarDay> taskDays = new HashSet<>();

        for (Task task : allTasks) {
            if (task.date != null && task.date.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
                String[] parts = task.date.split("-");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]) - 1;
                int day = Integer.parseInt(parts[2]);

                taskDays.add(CalendarDay.from(year, month, day));
            }
        }

        calendarView.addDecorator(new TaskDayDecorator(taskDays));
        calendarView.setOnDateChangedListener((@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) -> {
            String selectedDate = date.getYear() + "-" + (date.getMonth() + 1) + "-" + date.getDay();
            tasksLabel.setText("Tareas para: " + selectedDate);

            List<Task> filtered = allTasks.stream()
                    .filter(task -> task.date != null && task.date.equals(selectedDate))
                    .collect(Collectors.toList());

            adapter = new TaskAdapter(filtered, this);
            recyclerView.setAdapter(adapter);
        });
    }
}
