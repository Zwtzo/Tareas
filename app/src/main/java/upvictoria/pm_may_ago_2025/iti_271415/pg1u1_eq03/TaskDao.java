package upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.OnConflictStrategy;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Task task);


    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Query("SELECT * FROM Task")
    List<Task> getAllTasks();
    @Query("SELECT * FROM Task ORDER BY date ASC")
    List<Task> getAllTasksSorted();

    @Query("DELETE FROM Task WHERE id = :taskId")
    void deleteById(int taskId);
}
