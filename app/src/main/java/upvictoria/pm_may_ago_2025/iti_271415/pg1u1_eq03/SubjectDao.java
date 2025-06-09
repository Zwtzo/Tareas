package upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03.objects.Subject;

@Dao
public interface SubjectDao {
    @Insert
    void insert(Subject subject);

    @Query("DELETE FROM Subject WHERE subjectId = :subjectId")
    void deleteById(int subjectId);

    @Query("SELECT * FROM Subject ORDER BY name ASC")
    List<Subject> getAllSubjectsSorted();

    @Query("SELECT subjectId FROM Subject WHERE name = :name")
    int getSubjectIdByName(String name);
}
