package com.example.ensatecertnotes.ui.prof;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.dao.EtudiantDao;
import com.example.ensatecertnotes.db.dao.UserDao;
import com.example.ensatecertnotes.model.Etudiant;
import com.google.android.material.textfield.TextInputEditText;

public class AddStudentActivity extends AppCompatActivity {

    private TextInputEditText etCne, etNom, etPrenom, etFiliere, etAnnee, etEmail, etPassword;
    private Button btnSave, btnCancel;
    private TextView tvTitle;
    private LinearLayout layoutUserDetails;

    private EtudiantDao etudiantDao;
    private UserDao userDao;
    private int studentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        etudiantDao = new EtudiantDao(this);
        userDao = new UserDao(this);

        tvTitle = findViewById(R.id.tv_add_student_title);
        etCne = findViewById(R.id.et_student_cne);
        etNom = findViewById(R.id.et_student_nom);
        etPrenom = findViewById(R.id.et_student_prenom);
        etFiliere = findViewById(R.id.et_student_filiere);
        etAnnee = findViewById(R.id.et_student_annee);
        etEmail = findViewById(R.id.et_student_email);
        etPassword = findViewById(R.id.et_student_password);
        btnSave = findViewById(R.id.btn_save_student);
        btnCancel = findViewById(R.id.btn_cancel_student);
        layoutUserDetails = findViewById(R.id.layout_user_details);

        studentId = getIntent().getIntExtra("STUDENT_ID", -1);
        if (studentId != -1) {
            tvTitle.setText("Modifier Étudiant");
            layoutUserDetails.setVisibility(View.GONE); // Hide email/password when editing
            loadStudentData(studentId);
        }

        btnSave.setOnClickListener(v -> saveStudent());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void loadStudentData(int id) {
        Etudiant student = etudiantDao.getEtudiantById(id);
        if (student != null) {
            etCne.setText(student.getCne());
            etNom.setText(student.getNom());
            etPrenom.setText(student.getPrenom());
            etFiliere.setText(student.getFiliere());
            etAnnee.setText(student.getAnneeEtude());
        }
    }

    private void saveStudent() {
        String cne = etCne.getText().toString().trim();
        String nom = etNom.getText().toString().trim();
        String prenom = etPrenom.getText().toString().trim();
        String filiere = etFiliere.getText().toString().trim();
        String annee = etAnnee.getText().toString().trim();

        if (TextUtils.isEmpty(cne) || TextUtils.isEmpty(nom) || TextUtils.isEmpty(prenom) ||
                TextUtils.isEmpty(filiere) || TextUtils.isEmpty(annee)) {
            Toast.makeText(this, "Veuillez remplir les champs obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        if (studentId == -1) {
            // New Student: Requires account
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Email et mot de passe requis pour le nouveau compte", Toast.LENGTH_SHORT).show();
                return;
            }

            // 1. Create User
            long userId = userDao.createUser(email, password, "ETUDIANT");
            if (userId != -1) {
                // 2. Create Etudiant
                Etudiant etudiant = new Etudiant(0, (int) userId, cne, nom, prenom, filiere, annee, null);
                long result = etudiantDao.addStudent(etudiant);
                if (result != -1) {
                    Toast.makeText(this, "Étudiant créé avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Erreur lors de la création du profil", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Erreur lors de la création du compte", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Edit Student
            Etudiant existing = etudiantDao.getEtudiantById(studentId);
            if (existing != null) {
                existing.setCne(cne);
                existing.setNom(nom);
                existing.setPrenom(prenom);
                existing.setFiliere(filiere);
                existing.setAnneeEtude(annee);
                int result = etudiantDao.updateStudent(existing);
                if (result > 0) {
                    Toast.makeText(this, "Étudiant mis à jour", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
