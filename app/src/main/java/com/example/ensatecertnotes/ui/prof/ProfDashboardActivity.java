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

        tvName = findViewById(R.id.tv_prof_name);
        tvDept = findViewById(R.id.tv_prof_dept);
        rvModules = findViewById(R.id.rv_modules);
        btnLogout = findViewById(R.id.btn_logout);

        btnLogout.setOnClickListener(v -> session.logoutUser());

        loadProfData();
    }

    private void loadProfData() {
        int userId = session.getUserId();
        Professeur prof = professeurDao.getProfesseurByUserId(userId);

        if (prof != null) {
            tvName.setText("Bonjour, Pr. " + prof.getNom() + " " + prof.getPrenom());
            tvDept.setText("Département " + prof.getDepartement());

            loadModules(prof.getId());
        } else {
            Toast.makeText(this, "Erreur : Profil professeur introuvable", Toast.LENGTH_LONG).show();
            // session.logoutUser(); // Optional safety
        }
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
        }
    }
}
