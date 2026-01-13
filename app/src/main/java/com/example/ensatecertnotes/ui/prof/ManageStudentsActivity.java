package com.example.ensatecertnotes.ui.prof;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.dao.EtudiantDao;
import com.example.ensatecertnotes.model.Etudiant;
import com.example.ensatecertnotes.ui.adapters.StudentAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ManageStudentsActivity extends AppCompatActivity {

    private RecyclerView rvStudents;
    private StudentAdapter adapter;
    private EtudiantDao etudiantDao;
    private List<Etudiant> allStudents;
    private List<Etudiant> filteredStudents;

    private EditText etSearch;
    private Spinner spinnerSort;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_students);

        etudiantDao = new EtudiantDao(this);

        rvStudents = findViewById(R.id.rv_manage_students);
        etSearch = findViewById(R.id.et_search_student);
        spinnerSort = findViewById(R.id.spinner_sort_students);
        fabAdd = findViewById(R.id.fab_add_student);

        rvStudents.setLayoutManager(new LinearLayoutManager(this));

        loadStudents();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortStudents(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddStudentActivity.class);
            startActivity(intent);
        });
    }

    private void loadStudents() {
        allStudents = etudiantDao.getAllEtudiants();
        filteredStudents = new ArrayList<>(allStudents);
        adapter = new StudentAdapter(this, filteredStudents);
        rvStudents.setAdapter(adapter);
    }

    private void filter(String text) {
        filteredStudents.clear();
        for (Etudiant s : allStudents) {
            if (s.getNom().toLowerCase().contains(text.toLowerCase()) ||
                    s.getPrenom().toLowerCase().contains(text.toLowerCase()) ||
                    s.getCne().toLowerCase().contains(text.toLowerCase())) {
                filteredStudents.add(s);
            }
        }
        sortStudents(spinnerSort.getSelectedItemPosition());
    }

    private void sortStudents(int position) {
        switch (position) {
            case 0: // Nom A-Z
                Collections.sort(filteredStudents, (s1, s2) -> s1.getNom().compareToIgnoreCase(s2.getNom()));
                break;
            case 1: // Nom Z-A
                Collections.sort(filteredStudents, (s1, s2) -> s2.getNom().compareToIgnoreCase(s1.getNom()));
                break;
            case 2: // CNE
                Collections.sort(filteredStudents, (s1, s2) -> s1.getCne().compareToIgnoreCase(s2.getCne()));
                break;
        }
        adapter.updateList(filteredStudents);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudents(); // Refresh list when returning from Add/Edit
    }
}
