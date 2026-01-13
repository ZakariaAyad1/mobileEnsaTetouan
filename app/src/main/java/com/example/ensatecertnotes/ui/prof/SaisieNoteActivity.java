package com.example.ensatecertnotes.ui.prof;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.dao.NoteDao;
import com.example.ensatecertnotes.model.Note;

public class SaisieNoteActivity extends AppCompatActivity {

    private NoteDao noteDao;
    private int etudiantId, moduleId;
    private EditText etExam, etTd, etTp, etObs;
    private TextView tvStudentInfo, tvTitle, tvNoteFinale;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saisie_note);

        noteDao = new NoteDao(this);

        etudiantId = getIntent().getIntExtra("ETUDIANT_ID", -1);
        moduleId = getIntent().getIntExtra("MODULE_ID", -1);
        String etudiantName = getIntent().getStringExtra("ETUDIANT_NAME");
        String moduleName = getIntent().getStringExtra("MODULE_NAME");
        String etudiantCne = getIntent().getStringExtra("ETUDIANT_CNE");

        if (etudiantId == -1 || moduleId == -1) {
            Toast.makeText(this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvTitle = findViewById(R.id.tv_header_title);
        tvStudentInfo = findViewById(R.id.tv_student_info);
        etExam = findViewById(R.id.et_note_examen);
        etTd = findViewById(R.id.et_note_td);
        etTp = findViewById(R.id.et_note_tp);
        etObs = findViewById(R.id.et_observation);
        tvNoteFinale = findViewById(R.id.tv_note_finale);
        btnSave = findViewById(R.id.btn_save_note);

        tvTitle.setText("Notes : " + moduleName);
        tvStudentInfo.setText(etudiantName + "\n" + etudiantCne);

        setupTextWatchers();
        loadExistingNote();

        btnSave.setOnClickListener(v -> saveNote());
    }

    private void setupTextWatchers() {
        android.text.TextWatcher watcher = new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateTotal();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
            }
        };

        etExam.addTextChangedListener(watcher);
        etTd.addTextChangedListener(watcher);
        etTp.addTextChangedListener(watcher);
    }

    private void calculateTotal() {
        try {
            String strExam = etExam.getText().toString();
            String strTd = etTd.getText().toString();
            String strTp = etTp.getText().toString();

            double valExam = strExam.isEmpty() ? 0 : Double.parseDouble(strExam);
            double valTd = strTd.isEmpty() ? 0 : Double.parseDouble(strTd);
            double valTp = strTp.isEmpty() ? 0 : Double.parseDouble(strTp);

            // Formula: Exam * 0.4 + TD * 0.3 + TP * 0.3
            double total = (valExam * 0.4) + (valTd * 0.3) + (valTp * 0.3);

            // Format to 2 decimal places
            tvNoteFinale.setText(String.format(java.util.Locale.US, "%.2f / 20", total));

        } catch (NumberFormatException e) {
            tvNoteFinale.setText("-- / 20");
        }
    }

    private void loadExistingNote() {
        Note note = noteDao.getNote(etudiantId, moduleId);
        if (note != null) {
            if (note.getNoteExamen() != null)
                etExam.setText(String.valueOf(note.getNoteExamen()));
            if (note.getNoteTd() != null)
                etTd.setText(String.valueOf(note.getNoteTd()));
            if (note.getNoteTp() != null)
                etTp.setText(String.valueOf(note.getNoteTp()));
            if (note.getObservation() != null)
                etObs.setText(note.getObservation());

            // Allow manual set text to trigger watcher, but if note finale exists in DB,
            // rely on that?
            // Actually, recalculating is safer to ensure consistency with current inputs.
            // But let's check if DB value differs (e.g. rounded).
            calculateTotal();
        }
    }

    private void saveNote() {
        try {
            String strExam = etExam.getText().toString();
            String strTd = etTd.getText().toString();
            String strTp = etTp.getText().toString();

            Double valExam = strExam.isEmpty() ? null : Double.parseDouble(strExam);
            Double valTd = strTd.isEmpty() ? null : Double.parseDouble(strTd);
            Double valTp = strTp.isEmpty() ? null : Double.parseDouble(strTp);

            if (!validateNote(valExam) || !validateNote(valTd) || !validateNote(valTp)) {
                Toast.makeText(this, "Les notes doivent être entre 0 et 20.", Toast.LENGTH_SHORT).show();
                return;
            }

            Note note = new Note();
            note.setEtudiantId(etudiantId);
            note.setModuleId(moduleId);
            note.setNoteExamen(valExam);
            note.setNoteTd(valTd);
            note.setNoteTp(valTp);
            note.setObservation(etObs.getText().toString());

            noteDao.saveOrUpdateNote(note);

            // Since calculation is done by Trigger in DB, we rely on DB.
            // But we displayed the preview to the user.

            Toast.makeText(this, "Note enregistrée avec succès !", Toast.LENGTH_SHORT).show();
            finish(); // Retour à la liste
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Format de note invalide", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateNote(Double note) {
        if (note == null)
            return true;
        return note >= 0 && note <= 20;
    }
}
