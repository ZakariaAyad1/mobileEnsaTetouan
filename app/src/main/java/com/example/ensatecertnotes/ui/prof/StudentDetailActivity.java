package com.example.ensatecertnotes.ui.prof;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.dao.EtudiantDao;
import com.example.ensatecertnotes.model.Etudiant;

public class StudentDetailActivity extends AppCompatActivity {

    private TextView tvName, tvCne, tvFiliere, tvAnnee;
    private ImageView ivAvatar;
    private Button btnDelete;

    private EtudiantDao etudiantDao;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        etudiantDao = new EtudiantDao(this);
        studentId = getIntent().getIntExtra("STUDENT_ID", -1);

        tvName = findViewById(R.id.tv_detail_name);
        tvCne = findViewById(R.id.tv_detail_cne);
        tvFiliere = findViewById(R.id.tv_detail_filiere_val);
        tvAnnee = findViewById(R.id.tv_detail_annee);
        ivAvatar = findViewById(R.id.iv_detail_avatar);
        btnDelete = findViewById(R.id.btn_delete_student);

        loadStudentDetails();

        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    private void loadStudentDetails() {
        Etudiant s = etudiantDao.getEtudiantById(studentId);
        if (s != null) {
            tvName.setText(s.getNom() + " " + s.getPrenom());
            tvCne.setText(s.getCne());
            tvFiliere.setText(s.getFiliere());
            tvAnnee.setText(s.getAnneeEtude());
        } else {
            Toast.makeText(this, "Étudiant non trouvé", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Supprimer l'étudiant")
                .setMessage("Êtes-vous sûr de vouloir supprimer cet étudiant ? Cette action est irréversible.")
                .setPositiveButton("Supprimer", (dialog, which) -> {
                    int result = etudiantDao.deleteStudent(studentId);
                    if (result > 0) {
                        Toast.makeText(this, "Étudiant supprimé", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
}
