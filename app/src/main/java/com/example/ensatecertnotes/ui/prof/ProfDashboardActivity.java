package com.example.ensatecertnotes.ui.prof;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.dao.ModuleDao;
import com.example.ensatecertnotes.db.dao.NoteDao;
import com.example.ensatecertnotes.db.dao.ProfesseurDao;
import com.example.ensatecertnotes.model.Module;
import com.example.ensatecertnotes.model.Professeur;
import com.example.ensatecertnotes.ui.adapters.ModuleAdapter;
import com.example.ensatecertnotes.utils.SessionManager;

import java.util.List;
import java.util.Locale;

public class ProfDashboardActivity extends AppCompatActivity {

    private SessionManager session;
    private ProfesseurDao professeurDao;
    private ModuleDao moduleDao;
    private NoteDao noteDao;

    private TextView tvName, tvDept;
    private TextView tvStatModules, tvStatEtudiants, tvStatValidation;
    private RecyclerView rvModules;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_dashboard);

        session = new SessionManager(getApplicationContext());
        session.checkLogin(); // Redirect if not logged in

        professeurDao = new ProfesseurDao(this);
        moduleDao = new ModuleDao(this);
        noteDao = new NoteDao(this);

        tvName = findViewById(R.id.tv_prof_name);
        tvDept = findViewById(R.id.tv_prof_dept);

        tvStatModules = findViewById(R.id.tv_stat_modules);
        tvStatEtudiants = findViewById(R.id.tv_stat_etudiants);
        tvStatValidation = findViewById(R.id.tv_stat_validation);

        rvModules = findViewById(R.id.rv_modules);
        btnLogout = findViewById(R.id.btn_logout);

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> session.logoutUser());
        }

        View btnAdd = findViewById(R.id.btn_add_module_shortcut);
        if (btnAdd != null) {
            btnAdd.setOnClickListener(v -> {
                Intent intent = new Intent(this, AddModuleActivity.class);
                startActivity(intent);
            });
        }

        View btnManageStudents = findViewById(R.id.btn_manage_students);
        if (btnManageStudents != null) {
            btnManageStudents.setOnClickListener(v -> {
                Intent intent = new Intent(this, ManageStudentsActivity.class);
                startActivity(intent);
            });
        }

        View btnManageModules = findViewById(R.id.btn_manage_modules);
        if (btnManageModules != null) {
            btnManageModules.setOnClickListener(v -> {
                Intent intent = new Intent(this, ManageModulesActivity.class);
                startActivity(intent);
            });
        }

        loadProfData();
    }

    private void loadProfData() {
        try {
            int userId = session.getUserId();
            Professeur prof = professeurDao.getProfesseurByUserId(userId);

            if (prof != null) {
                if (tvName != null)
                    tvName.setText("Bonjour, Pr. " + prof.getNom() + " " + prof.getPrenom());
                if (tvDept != null)
                    tvDept.setText("Département " + prof.getDepartement());

                loadModules(prof.getId());
                loadStatistics(prof.getId());
            } else {
                Toast.makeText(this, "Profil professeur introuvable (UserId: " + userId + ")", Toast.LENGTH_LONG)
                        .show();
            }
        } catch (Exception e) {
            Log.e("ProfDashboard", "Error loading data", e);
            Toast.makeText(this, "Erreur de chargement des données", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadStatistics(int profId) {
        // 1. Modules Count
        List<Module> modules = moduleDao.getModulesByProfesseur(profId);
        if (tvStatModules != null)
            tvStatModules.setText(String.valueOf(modules.size()));

        // 2. Students Count
        int studentCount = moduleDao.countStudentsByProfesseur(profId);
        if (tvStatEtudiants != null)
            tvStatEtudiants.setText(String.valueOf(studentCount));

        // 3. Validation Rate
        double validationRate = noteDao.getValidationRateByProfesseur(profId);
        if (tvStatValidation != null) {
            tvStatValidation.setText(String.format(Locale.US, "%.0f%%", validationRate));
        }
    }

    private void loadModules(int profId) {
        List<Module> modules = moduleDao.getModulesByProfesseur(profId);
        if (rvModules != null) {
            ModuleAdapter adapter = new ModuleAdapter(this, modules);
            rvModules.setLayoutManager(new LinearLayoutManager(this));
            rvModules.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!session.isLoggedIn()) {
            finish();
            return;
        }
        loadProfData(); // Refresh stats and modules list
    }
}
