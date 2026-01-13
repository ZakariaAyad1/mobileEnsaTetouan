
package com.example.ensatecertnotes.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.DatabaseHelper;
import com.example.ensatecertnotes.db.dao.EtudiantDao_etudiant;
import com.example.ensatecertnotes.model.Etudiant;
import com.example.ensatecertnotes.model.User;
import com.example.ensatecertnotes.utils.SessionManager;

public class LoginEtudiantActivity_etudiant extends AppCompatActivity {
    
    private EditText etCne;
    private EditText etPassword;
    private Button btnLogin;
    
    private DatabaseHelper dbHelper;
    private EtudiantDao_etudiant etudiantDao;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_etudiant);

        // Initialize database and session
        dbHelper = DatabaseHelper.getInstance(this);
        etudiantDao = new EtudiantDao_etudiant(dbHelper);
        sessionManager = new SessionManager(this);

        // Initialize views
        etCne = findViewById(R.id.et_cne);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        // Set login button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginStudent();
            }
        });
    }

    private void loginStudent() {
        String identifier = etCne.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate inputs
        if (identifier.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Get student by CNE or Email
            Etudiant etudiant = etudiantDao.getEtudiantByCneOrEmail(identifier);
            
            if (etudiant == null) {
                Toast.makeText(this, "Identifiant incorrect (CNE ou Email introuvable)", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get user and verify password
            User user = getUserById(etudiant.getUserId());
            
            if (user == null) {
                Toast.makeText(this, "Erreur système : Utilisateur introuvable (ID: " + etudiant.getUserId() + ")", Toast.LENGTH_LONG).show();
                return;
            }

            if (user.getPassword().equals(password)) {
                // Check role
                if (!"ETUDIANT".equals(user.getRole())) {
                    Toast.makeText(this, "Accès non autorisé : Rôle " + user.getRole(), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create session
                sessionManager.createLoginSession(
                    user.getId(),
                    user.getRole(),
                    etudiant.getNom() + " " + etudiant.getPrenom(),
                    etudiant.getId()
                );

                Toast.makeText(this, "Connexion réussie !", Toast.LENGTH_SHORT).show();

                // Navigate to dashboard
                Intent intent = new Intent(LoginEtudiantActivity_etudiant.this, 
                    DashboardEtudiant_etudiant.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Mot de passe incorrect", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur de connexion : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private User getUserById(int userId) {
        // Simple method to get user from database
        android.database.Cursor cursor = dbHelper.getReadableDatabase()
            .rawQuery("SELECT * FROM users WHERE id = ?", 
                new String[]{String.valueOf(userId)});
        
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            user.setRole(cursor.getString(cursor.getColumnIndexOrThrow("role")));
        }
        cursor.close();
        return user;
    }
}
