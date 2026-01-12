package com.example.ensatecertnotes.ui.prof;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.dao.ModuleDao;
import com.example.ensatecertnotes.model.Module;
import com.example.ensatecertnotes.ui.adapters.ModuleAdapter;
import com.example.ensatecertnotes.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ManageModulesActivity extends AppCompatActivity {

    private ModuleDao moduleDao;
    private SessionManager session;
    private RecyclerView rvModules;
    private ModuleAdapter adapter;
    private List<Module> allModules;
    private int profId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_modules);

        session = new SessionManager(this);
        session.checkLogin();

        // In a real app, we'd get profId from the ProfileDao using userId
        // For now, let's assume we can fetch modules by the current professor's ID
        // Note: ProfDashboardActivity already does this, so we should be consistent.
        // I'll need to fetch the professor object first.
        com.example.ensatecertnotes.db.dao.ProfesseurDao profDao = new com.example.ensatecertnotes.db.dao.ProfesseurDao(
                this);
        com.example.ensatecertnotes.model.Professeur prof = profDao.getProfesseurByUserId(session.getUserId());

        if (prof != null) {
            profId = prof.getId();
        }

        moduleDao = new ModuleDao(this);
        rvModules = findViewById(R.id.rv_manage_modules);
        EditText etSearch = findViewById(R.id.et_search_module);

        findViewById(R.id.fab_add_module).setOnClickListener(v -> {
            startActivity(new Intent(this, AddModuleActivity.class));
        });

        loadModules();

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterModules(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void loadModules() {
        allModules = moduleDao.getModulesByProfesseur(profId);
        adapter = new ModuleAdapter(this, allModules);
        rvModules.setLayoutManager(new LinearLayoutManager(this));
        rvModules.setAdapter(adapter);
    }

    private void filterModules(String query) {
        if (allModules == null)
            return;

        List<Module> filtered = allModules.stream()
                .filter(m -> m.getNomModule().toLowerCase().contains(query.toLowerCase()) ||
                        m.getCodeModule().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        adapter = new ModuleAdapter(this, filtered);
        rvModules.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadModules();
    }
}
