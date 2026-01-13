
package com.example.ensatecertnotes.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.DatabaseHelper;
import com.example.ensatecertnotes.db.dao.NoteDao_etudiant;
import com.example.ensatecertnotes.utils.SessionManager;

public class DetailModuleActivity_etudiant extends AppCompatActivity {
    
    private TextView tvModuleCode;
    private TextView tvModuleName;
    private TextView tvCredits;
    private TextView tvNoteExamen;
    private TextView tvNoteTd;
    private TextView tvNoteTp;
    private TextView tvNoteFinale;
    private TextView tvStatut;
    private TextView tvObservation;
    
    private DatabaseHelper dbHelper;
    private NoteDao_etudiant noteDao;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_module_etudiant);

        // Initialize database and session
        dbHelper = DatabaseHelper.getInstance(this);
        noteDao = new NoteDao_etudiant(dbHelper);
        sessionManager = new SessionManager(this);

        // Set up toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Détails du Module");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        tvModuleCode = findViewById(R.id.tv_module_code);
        tvModuleName = findViewById(R.id.tv_module_name);
        tvCredits = findViewById(R.id.tv_credits);
        tvNoteExamen = findViewById(R.id.tv_note_examen);
        tvNoteTd = findViewById(R.id.tv_note_td);
        tvNoteTp = findViewById(R.id.tv_note_tp);
        tvNoteFinale = findViewById(R.id.tv_note_finale);
        tvStatut = findViewById(R.id.tv_statut);
        tvObservation = findViewById(R.id.tv_observation);

        // Get data from intent
        Intent intent = getIntent();
        int moduleId = intent.getIntExtra("MODULE_ID", -1);
        int etudiantId = sessionManager.getEtudiantId();

        if (moduleId != -1 && etudiantId != -1) {
            loadModuleDetails(etudiantId, moduleId);
        }
    }

    private void loadModuleDetails(int etudiantId, int moduleId) {
        NoteDao_etudiant.NoteWithModule noteWithModule = noteDao.getNotesDetailsByModule(etudiantId, moduleId);

        if (noteWithModule != null) {
            // Set module info
            tvModuleCode.setText(noteWithModule.getModuleCode());
            tvModuleName.setText(noteWithModule.getModuleNom());
            tvCredits.setText(String.valueOf(noteWithModule.getCredits()) + " crédits");

            // Set grades
            tvNoteExamen.setText(formatNote(noteWithModule.getNote().getNoteExamen()));
            tvNoteTd.setText(formatNote(noteWithModule.getNote().getNoteTd()));
            tvNoteTp.setText(formatNote(noteWithModule.getNote().getNoteTp()));
            tvNoteFinale.setText(formatNote(noteWithModule.getNote().getNoteFinale()));

            // Set status
            String statut = noteWithModule.getNote().getStatut();
            tvStatut.setText(statut != null ? statut : "Non évalué");
            
            // Set status color
            if ("VALIDE".equals(statut)) {
                tvStatut.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else if ("NON_VALIDE".equals(statut)) {
                tvStatut.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }

            // Set observation
            String observation = noteWithModule.getNote().getObservation();
            tvObservation.setText(observation != null ? observation : "Aucune observation");
        }
    }

    private String formatNote(Double note) {
        if (note == null) {
            return "-";
        }
        return String.format("%.2f / 20", note);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

