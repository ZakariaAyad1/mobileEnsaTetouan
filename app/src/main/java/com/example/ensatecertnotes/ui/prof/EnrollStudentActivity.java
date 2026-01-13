package com.example.ensatecertnotes.ui.prof;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.dao.EtudiantDao;
import com.example.ensatecertnotes.db.dao.InscriptionDao;
import com.example.ensatecertnotes.model.Etudiant;
import com.example.ensatecertnotes.ui.adapters.StudentAdapter;

import java.util.List;
import java.util.stream.Collectors;

public class EnrollStudentActivity extends AppCompatActivity {

    private EtudiantDao etudiantDao;
    private InscriptionDao inscriptionDao;
    private int moduleId;
    private List<Etudiant> allStudents;
    private RecyclerView rvStudents;
    private StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_student);

        etudiantDao = new EtudiantDao(this);
        inscriptionDao = new InscriptionDao(this);
        moduleId = getIntent().getIntExtra("MODULE_ID", -1);

        if (moduleId == -1) {
            finish();
            return;
        }

        rvStudents = findViewById(R.id.rv_enroll_students);
        EditText etSearch = findViewById(R.id.et_search_all_students);

        loadStudents();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterStudents(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void loadStudents() {
        allStudents = etudiantDao.getAllEtudiants();
        // Here we can filter out those already enrolled if we want, but simpler to just
        // handle it on click
        adapter = new StudentAdapter(this, allStudents);

        // Overriding the click to enroll instead of showing details
        adapter.setOnStudentClickListener(etudiant -> {
            if (inscriptionDao.isStudentEnrolled(etudiant.getId(), moduleId)) {
                Toast.makeText(this, "Cet étudiant est déjà inscrit.", Toast.LENGTH_SHORT).show();
            } else {
                long res = inscriptionDao.enrollStudent(etudiant.getId(), moduleId, "2023-2024");
                if (res != -1) {
                    Toast.makeText(this, "Étudiant inscrit avec succès !", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Erreur lors de l'inscription.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rvStudents.setLayoutManager(new LinearLayoutManager(this));
        rvStudents.setAdapter(adapter);
    }

    private void filterStudents(String query) {
        if (allStudents == null)
            return;
        List<Etudiant> filtered = allStudents.stream()
                .filter(e -> e.getNom().toLowerCase().contains(query.toLowerCase()) ||
                        e.getPrenom().toLowerCase().contains(query.toLowerCase()) ||
                        e.getCne().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        adapter = new StudentAdapter(this, filtered);
        rvStudents.setAdapter(adapter);
    }
}
