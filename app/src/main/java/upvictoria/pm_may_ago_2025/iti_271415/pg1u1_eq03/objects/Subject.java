package upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03.objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Subject implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int subjectId;

    @ColumnInfo(name = "name")
    public String name;

    @NonNull
    @Override
    public String toString() {
        return getName(); // o el campo que almacene el nombre de la materia
    }

    public String getName() {
        return name;
    }

    public Subject(String name) {
        this.name = name;
    }

    public Subject() {} // Constructor vacío requerido por Room
}
