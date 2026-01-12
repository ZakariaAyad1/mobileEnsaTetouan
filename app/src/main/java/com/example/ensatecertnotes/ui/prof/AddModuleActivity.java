package com.example.ensatecertnotes.ui.prof;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.dao.ModuleDao;
import com.example.ensatecertnotes.model.Module;
import com.example.ensatecertnotes.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

public class AddModuleActivity extends AppCompatActivity {

    private TextInputEditText etCode, etName, etSemestre, etCoeff, etYear;
    private Button btnSave, btnCancel;
    private TextView tvTitle;

    private ModuleDao moduleDao;
    private SessionManager session;
    private int moduleId = -1; // -1 means adding new, otherwise editing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_module);

        moduleDao = new ModuleDao(this);
        session = new SessionManager(this);

        tvTitle = findViewById(R.id.tv_add_module_title);
        etCode = findViewById(R.id.et_module_code);
        etName = findViewById(R.id.et_module_name);
        etSemestre = findViewById(R.id.et_module_semestre);
        etCoeff = findViewById(R.id.et_module_coeff);
        etYear = findViewById(R.id.et_module_year);
        btnSave = findViewById(R.id.btn_save_module);
        btnCancel = findViewById(R.id.btn_cancel_module);

        // Check if we are editing
        moduleId = getIntent().getIntExtra("MODULE_ID", -1);
        if (moduleId != -1) {
            tvTitle.setText("Modifier le Module");
            loadModuleData(moduleId);
        }

        btnSave.setOnClickListener(v -> saveModule());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void loadModuleData(int id) {
        Module module = moduleDao.getModuleById(id);
        if (module != null) {
            etCode.setText(module.getCodeModule());
            etName.setText(module.getNomModule());
            etSemestre.setText(String.valueOf(module.getSemestre()));
            etCoeff.setText(String.valueOf(module.getCoefficient()));
            etYear.setText(module.getAnneeUniversitaire());
        }
    }

    private void saveModule() {
        String code = etCode.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String semestreStr = etSemestre.getText().toString().trim();
        String coeffStr = etCoeff.getText().toString().trim();
        String year = etYear.getText().toString().trim();

        if (TextUtils.isEmpty(code) || TextUtils.isEmpty(name) || TextUtils.isEmpty(semestreStr) ||
                TextUtils.isEmpty(coeffStr) || TextUtils.isEmpty(year)) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int semestre = Integer.parseInt(semestreStr);
            double coeff = Double.parseDouble(coeffStr);
            int profId = -1;

            // We need the professor's ID, which should be stored in session or fetched
            // For now, let's assume we can get it from the session (need to check
            // SessionManager)
            // Or we fetch it once from ProfesseurDao using session.getUserId()
            com.example.ensatecertnotes.db.dao.ProfesseurDao profDao = new com.example.ensatecertnotes.db.dao.ProfesseurDao(
                    this);
            com.example.ensatecertnotes.model.Professeur prof = profDao.getProfesseurByUserId(session.getUserId());
            if (prof != null) {
                profId = prof.getId();
            } else {
                Toast.makeText(this, "Erreur: Professeur non identifié", Toast.LENGTH_SHORT).show();
                return;
            }

            Module module = new Module(moduleId, code, name, semestre, coeff, profId, year);

            if (moduleId == -1) {
                long result = moduleDao.addModule(module);
                if (result != -1) {
                    Toast.makeText(this, "Module ajouté avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
                }
            } else {
                int result = moduleDao.updateModule(module);
                if (result > 0) {
                    Toast.makeText(this, "Module mis à jour", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Veuillez entrer des nombres valides pour semestre et coefficient", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
