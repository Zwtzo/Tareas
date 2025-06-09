package upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03;

import androidx.room.Dao;
import androidx.room.Delete;
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

    @Delete
    void delete(Task task);

    @Update
    void update(Task task);

    @Query("SELECT * FROM Task ORDER BY CASE WHEN status = 'Completada' THEN 1 ELSE 0 END, date ASC")
    List<Task> getAllTasksSorted();

    @Query("SELECT * FROM Task WHERE status = :status ORDER BY date ASC")
    List<Task> getTasksByStatus(String status);

    @Query("DELETE FROM Task WHERE id = :taskId")
    void deleteById(int taskId);
}
