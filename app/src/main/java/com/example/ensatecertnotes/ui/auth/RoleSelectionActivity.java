package com.example.ensatecertnotes.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ensatecertnotes.R;
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
            // Basic redirection based on role stored (can be refined)
            // For now, let's allow them to re-select or logout.
            // Or redirect to their dashboard if we knew which activity.
            // We can check role and redirect.
            String role = session.getRole();
            if ("PROFESSEUR".equals(role)) {
                // Redirect to Prof Dashboard
                // startActivity(new Intent(this,
                // com.example.ensatecertnotes.ui.prof.ProfDashboardActivity.class));
                // finish();
            }
        }

        Button btnProf = findViewById(R.id.btn_professeur);
        Button btnEtu = findViewById(R.id.btn_etudiant);
        Button btnAdmin = findViewById(R.id.btn_admin);

        btnProf.setOnClickListener(v -> openLogin("PROFESSEUR"));
        btnEtu.setOnClickListener(v -> openLogin("ETUDIANT"));
        btnAdmin.setOnClickListener(v -> openLogin("ADMIN")); // Will handle AGENT/MANAGER logic later
    }

    private void openLogin(String role) {
        Intent intent = new Intent(RoleSelectionActivity.this, LoginActivity.class);
        intent.putExtra("TARGET_ROLE", role);
        startActivity(intent);
    }
}
