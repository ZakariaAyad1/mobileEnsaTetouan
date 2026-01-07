package com.example.ensatecertnotes.ui.prof;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.dao.ModuleDao;
import com.example.ensatecertnotes.db.dao.ProfesseurDao;
import com.example.ensatecertnotes.model.Module;
import com.example.ensatecertnotes.model.Professeur;
import com.example.ensatecertnotes.ui.adapters.ModuleAdapter;
import com.example.ensatecertnotes.utils.SessionManager;

import java.util.List;

public class ProfDashboardActivity extends AppCompatActivity {

    private SessionManager session;
    private ProfesseurDao professeurDao;
    private ModuleDao moduleDao;

    private TextView tvName, tvDept;
    private TextView tvStatModules, tvStatEtudiants, tvStatValidation;
    private RecyclerView rvModules;
    private Button btnLogout;
    private com.example.ensatecertnotes.db.dao.NoteDao noteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_dashboard);

        session = new SessionManager(getApplicationContext());
        session.checkLogin(); // Redirect if not logged in

        professeurDao = new ProfesseurDao(this);
        moduleDao = new ModuleDao(this);
        noteDao = new com.example.ensatecertnotes.db.dao.NoteDao(this);

        tvName = findViewById(R.id.tv_prof_name);
        tvDept = findViewById(R.id.tv_prof_dept);

        tvStatModules = findViewById(R.id.tv_stat_modules);
        tvStatEtudiants = findViewById(R.id.tv_stat_etudiants);
        tvStatValidation = findViewById(R.id.tv_stat_validation);

        rvModules = findViewById(R.id.rv_modules);
        btnLogout = findViewById(R.id.btn_logout);

        btnLogout.setOnClickListener(v -> session.logoutUser());

        findViewById(R.id.btn_add_module_shortcut).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, AddModuleActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_manage_students).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(this, ManageStudentsActivity.class);
            startActivity(intent);
        });

        loadProfData();
    }

    private void loadProfData() {
        int userId = session.getUserId();
        Professeur prof = professeurDao.getProfesseurByUserId(userId);

        if (prof != null) {
            tvName.setText("Bonjour, Pr. " + prof.getNom() + " " + prof.getPrenom());
            tvDept.setText("Département " + prof.getDepartement());

            loadModules(prof.getId());
            loadStatistics(prof.getId());
        } else {
            Toast.makeText(this, "Erreur : Profil professeur introuvable", Toast.LENGTH_LONG).show();
        }
    }

    private void loadStatistics(int profId) {
        // 1. Modules Count
        List<Module> modules = moduleDao.getModulesByProfesseur(profId);
        tvStatModules.setText(String.valueOf(modules.size())); // Already fetched, can just size() or query

        // 2. Students Count
        int studentCount = moduleDao.countStudentsByProfesseur(profId);
        tvStatEtudiants.setText(String.valueOf(studentCount));

        // 3. Validation Rate
        double validationRate = noteDao.getValidationRateByProfesseur(profId);
        tvStatValidation.setText(String.format(java.util.Locale.US, "%.0f%%", validationRate));
    }

    private void loadModules(int profId) {
        List<Module> modules = moduleDao.getModulesByProfesseur(profId);
        ModuleAdapter adapter = new ModuleAdapter(this, modules);
        rvModules.setLayoutManager(new LinearLayoutManager(this));
        rvModules.setAdapter(adapter);
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
