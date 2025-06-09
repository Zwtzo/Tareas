package upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03.objects.Subject;
import upvictoria.pm_may_ago_2025.iti_271415.pg1u1_eq03.objects.Task;

public class SubjectFormActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SubjectAdapter subjectAdapter;
    private List<Subject> subjectList;
    private EditText nameInput;
    private Button saveBtn;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_form);

        // Inicializar los campos de entrada y el botón
        nameInput = findViewById(R.id.nameInput);
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // no se necesita implementación aquí
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // tampoco aquí
                // apoco no
            }

            @Override
            public void afterTextChanged(Editable s) {
                enableSaveButton();
            }
        });

        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setEnabled(false);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationOnClickListener(v -> finish());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        myToolbar.setTitle("Materias");

        subjectList = TaskDatabase.getInstance(this).subjectDao().getAllSubjectsSorted();
        subjectAdapter = new SubjectAdapter(subjectList, this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(subjectAdapter);

        if(subjectList.isEmpty()) {
            TextView aviso = findViewById(R.id.aviso);
            aviso.setVisibility(TextView.VISIBLE);
        }

        saveBtn.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            Subject subject = new Subject(name);

            TaskDatabase.getInstance(this).subjectDao().insert(subject);
            nameInput.setText("");
            enableSaveButton();

            subjectList.clear();
            subjectList.addAll(TaskDatabase.getInstance(this).subjectDao().getAllSubjectsSorted());
            subjectAdapter.notifyDataSetChanged();
            TextView aviso = findViewById(R.id.aviso);
            aviso.setVisibility(TextView.GONE);
        });
    }

    protected void enableSaveButton(){
        String name = nameInput.getText().toString().trim();
        saveBtn.setEnabled(!name.isEmpty());

        if (TaskDatabase.getInstance(this).subjectDao().getSubjectIdByName(name) > 0) {
            saveBtn.setEnabled(false);
            nameInput.setError("Ya existe una materia con este nombre");
        } else {
            nameInput.setError(null);
        }
    }
}
