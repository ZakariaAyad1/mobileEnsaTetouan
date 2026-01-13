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
import com.example.ensatecertnotes.db.dao.NotificationDao_etudiant; // salma
import com.example.ensatecertnotes.ui.admin.NotificationActivity_etudiant; // salma

public class DashboardEtudiant_etudiant extends AppCompatActivity {
    
    private SessionManager sessionManager;
    /*salma*/
    private TextView tvNotificationBadge;
    private NotificationDao_etudiant notificationDao;
    /*salma*/

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


        /*salma*/
        tvNotificationBadge = findViewById(R.id.tv_notification_badge_student);
        notificationDao = new NotificationDao_etudiant(this);

        findViewById(R.id.layout_notification_student).setOnClickListener(v -> {
            Intent intent = new Intent(DashboardEtudiant_etudiant.this, NotificationActivity_etudiant.class);
            intent.putExtra("IS_STUDENT", true);
            startActivity(intent);
        });
        /*salma*/
    }

    /*salma*/
    @Override
    protected void onResume() {
        super.onResume();
        checkNotifications();
    }

    private void checkNotifications() {
        if (sessionManager.isLoggedIn()) {
            int userId = sessionManager.getUserId();
            int unreadCount = notificationDao.getUnreadCount(userId);
            
             if (unreadCount > 0) {
                tvNotificationBadge.setText(String.valueOf(unreadCount));
                tvNotificationBadge.setVisibility(View.VISIBLE);
            } else {
                tvNotificationBadge.setVisibility(View.GONE);
            }
        }
    }
    /*salma*/

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginEtudiantActivity_etudiant.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
/*salma*/
