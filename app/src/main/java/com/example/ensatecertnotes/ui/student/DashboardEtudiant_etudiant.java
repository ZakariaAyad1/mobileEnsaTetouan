/*salma*/
package com.example.ensatecertnotes.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.utils.SessionManager;

public class DashboardEtudiant_etudiant extends AppCompatActivity {
    
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_etudiant);

        // Initialize session manager
        sessionManager = new SessionManager(this);

        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            navigateToLogin();
            return;
        }

        // Setup Bottom Navigation
        com.google.android.material.bottomnavigation.BottomNavigationView navView = 
            findViewById(R.id.nav_view);
        
        // Initial fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_activity_dashboard, new HomeFragment_etudiant())
                .commit();
        }

        navView.setOnItemSelectedListener(item -> {
            androidx.fragment.app.Fragment selectedFragment = null;
            int itemId = item.getItemId();
            
            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment_etudiant();
            } else if (itemId == R.id.nav_suivi) {
                selectedFragment = new SuiviFragment_etudiant();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfilFragment_etudiant();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_dashboard, selectedFragment)
                    .commit();
                return true;
            }
            return false;
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginEtudiantActivity_etudiant.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
/*salma*/
