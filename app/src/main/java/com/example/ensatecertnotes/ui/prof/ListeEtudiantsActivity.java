package com.example.ensatecertnotes.ui.prof;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.dao.EtudiantDao;
import com.example.ensatecertnotes.model.Etudiant;
import com.example.ensatecertnotes.ui.adapters.EtudiantAdapter;

import java.util.List;

public class ListeEtudiantsActivity extends AppCompatActivity {

    private EtudiantDao etudiantDao;
    private RecyclerView rvEtudiants;
    private TextView tvModuleTitle;
    private int moduleId;
    private String moduleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_etudiants);

        etudiantDao = new EtudiantDao(this);
        rvEtudiants = findViewById(R.id.rv_etudiants);
        tvModuleTitle = findViewById(R.id.tv_module_title);

        moduleId = getIntent().getIntExtra("MODULE_ID", -1);
        moduleName = getIntent().getStringExtra("MODULE_NAME");

        if (moduleId == -1) {
            Toast.makeText(this, "Module invalide", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvModuleTitle.setText("Module : " + (moduleName != null ? moduleName : "Inconnu"));

        loadEtudiants();
    }

    private void loadEtudiants() {
        List<Etudiant> etudiants = etudiantDao.getEtudiantsByModule(moduleId);
        if (etudiants.isEmpty()) {
            Toast.makeText(this, "Aucun étudiant inscrit à ce module.", Toast.LENGTH_SHORT).show();
        }

        EtudiantAdapter adapter = new EtudiantAdapter(this, etudiants, moduleId, moduleName);
        rvEtudiants.setLayoutManager(new LinearLayoutManager(this));
        rvEtudiants.setAdapter(adapter);
    }
}
