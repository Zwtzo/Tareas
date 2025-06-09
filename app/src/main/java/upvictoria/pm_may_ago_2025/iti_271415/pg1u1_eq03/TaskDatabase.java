package upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03.objects.Subject;
import upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03.objects.Task;

@Database(entities = {Task.class, Subject.class}, version = 1)
public abstract class TaskDatabase extends RoomDatabase {
    private static TaskDatabase INSTANCE;

    public abstract TaskDao taskDao();

    public abstract SubjectDao subjectDao();

    public static synchronized TaskDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskDatabase.class, "task_db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
