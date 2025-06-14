package upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03.objects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Task implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "status")
    public String status; // "Iniciado", "En proceso", "Completado"

    @ColumnInfo(name = "subject")
    public String subject;

    public Task(String title, String description, String date, String status, String subject) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.status = status;
        this.subject = subject;
    }

    // Constructor vacío requerido por Room
    public Task() {}
}
