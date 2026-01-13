package com.example.ensatecertnotes.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.dao.UserDao;
import com.example.ensatecertnotes.model.User;
import com.example.ensatecertnotes.ui.prof.ProfDashboardActivity;
import com.example.ensatecertnotes.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvTitle;
    private UserDao userDao;
    private SessionManager session;
    private String targetRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDao = new UserDao(this);
        session = new SessionManager(getApplicationContext());

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvTitle = findViewById(R.id.tv_login_title);

        targetRole = getIntent().getStringExtra("TARGET_ROLE");
        if (targetRole != null) {
            String title = "Connexion ";
            switch (targetRole) {
                case "PROFESSEUR":
                    title += "Professeur";
                    break;
                case "ETUDIANT":
                    title += "Étudiant";
                    break;
                case "ADMIN":
                    title += "Administration";
                    break;
            }
            tvTitle.setText(title);
        }

        btnLogin.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = userDao.login(email, password);

        if (user != null) {
            // Check Role consistency
            // Note: DB roles are 'PROFESSEUR', 'ETUDIANT', 'AGENT', 'MANAGER'
            // 'ADMIN' target role matches 'AGENT' or 'MANAGER'

            boolean roleMatches = false;
            if (targetRole == null || targetRole.equals(user.getRole())) {
                roleMatches = true;
            } else if ("ADMIN".equals(targetRole)
                    && (user.getRole().equals("AGENT") || user.getRole().equals("MANAGER"))) {
                roleMatches = true;
            }

            if (roleMatches) {
                session.createLoginSession(user.getId(), user.getEmail(), user.getRole());

                // Redirect based on actual user role
                if ("PROFESSEUR".equals(user.getRole())) {
                    startActivity(new Intent(LoginActivity.this, ProfDashboardActivity.class));
                } else if ("ETUDIANT".equals(user.getRole())) {
                    // startActivity(new Intent(LoginActivity.this,
                    // StudentDashboardActivity.class));
                    Toast.makeText(this, "Espace Étudiant en construction", Toast.LENGTH_SHORT).show();
                } else {
                    // Admin
                    // startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                    Toast.makeText(this, "Espace Admin en construction", Toast.LENGTH_SHORT).show();
                }
                finish();
            } else {
                Toast.makeText(this, "Ce compte n'a pas accès à cet espace.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
        }
    }
}
