package com.example.ensatecertnotes.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.ui.admin.LoginAdminActivity;
import com.example.ensatecertnotes.utils.SessionManager;

public class RoleSelectionActivity extends AppCompatActivity {

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in (Optional: Redirect to dashboard directly)
        if (session.isLoggedIn()) {
            String role = session.getRole();
            if ("PROFESSEUR".equals(role)) {
                // Redirect to Prof Dashboard
                // startActivity(new Intent(this, ProfDashboardActivity.class));
                // finish();
            } else if ("ETUDIANT".equals(role)) {
                // Redirect to Student Dashboard
                // startActivity(new Intent(this, StudentDashboardActivity.class));
                // finish();
            } else if ("AGENT".equals(role) || "MANAGER".equals(role)) {
                // Redirect to Admin Dashboard
                startActivity(new Intent(this, com.example.ensatecertnotes.ui.admin.DashboardAdminActivity.class));
                finish();
            }
        }

        Button btnProf = findViewById(R.id.btn_professeur);
        Button btnEtu = findViewById(R.id.btn_etudiant);
        Button btnAdmin = findViewById(R.id.btn_admin);

        btnProf.setOnClickListener(v -> openLogin("PROFESSEUR"));
        
        btnEtu.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectionActivity.this, 
                com.example.ensatecertnotes.ui.student.LoginEtudiantActivity_etudiant.class);
            startActivity(intent);
        });
        
        // ✅ MODIFIÉ : Navigation vers LoginAdminActivity
        btnAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectionActivity.this, LoginAdminActivity.class);
            startActivity(intent);
        });
    }

    private void openLogin(String role) {
        Intent intent = new Intent(RoleSelectionActivity.this, LoginActivity.class);
        intent.putExtra("TARGET_ROLE", role);
        startActivity(intent);
    }
}