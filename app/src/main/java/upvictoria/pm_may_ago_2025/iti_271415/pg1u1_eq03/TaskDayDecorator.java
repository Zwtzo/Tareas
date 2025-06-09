package upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.HashSet;
import java.util.Set;

public class TaskDayDecorator implements DayViewDecorator {

    private final Set<CalendarDay> daysWithTasks;

    public TaskDayDecorator(Set<CalendarDay> taskDays) {
        this.daysWithTasks = taskDays;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return daysWithTasks.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.RED));
        // view.setBackgroundDrawable(...);
    }
}
