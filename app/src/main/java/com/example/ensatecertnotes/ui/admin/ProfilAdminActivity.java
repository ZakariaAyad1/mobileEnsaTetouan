package com.example.ensatecertnotes.ui.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.ui.auth.RoleSelectionActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfilAdminActivity extends AppCompatActivity {

    private TextView tvNom, tvPrenom, tvEmail, tvFonction, tvRole;
    private Button btnDeconnexion;
    private SharedPreferences sharedPreferences;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_admin);

        sharedPreferences = getSharedPreferences("AdminSession", MODE_PRIVATE);

        // Initialisation
        tvNom = findViewById(R.id.tv_profil_nom);
        tvPrenom = findViewById(R.id.tv_profil_prenom);
        tvEmail = findViewById(R.id.tv_profil_email);
        tvFonction = findViewById(R.id.tv_profil_fonction);
        tvRole = findViewById(R.id.tv_profil_role);
        btnDeconnexion = findViewById(R.id.btn_deconnexion);
        bottomNav = findViewById(R.id.bottom_nav_admin);

        // Charger les infos
        loadProfil();

        // Déconnexion
        btnDeconnexion.setOnClickListener(v -> showLogoutDialog());

        // Bottom Navigation
        bottomNav.setSelectedItemId(R.id.nav_profil);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_dashboard) {
                startActivity(new Intent(this, DashboardAdminActivity.class));
                return true;
            } else if (itemId == R.id.nav_demandes) {
                startActivity(new Intent(this, ListeDemandesActivity.class));
                return true;
            } else if (itemId == R.id.nav_etudiants) {
                startActivity(new Intent(this, GestionEtudiantsActivity.class));
                return true;
            } else if (itemId == R.id.nav_profil) {
                return true;
            }
            return false;
        });
    }

    private void loadProfil() {
        String nom = sharedPreferences.getString("nom", "N/A");
        String prenom = sharedPreferences.getString("prenom", "N/A");
        String email = sharedPreferences.getString("email", "N/A");
        String fonction = sharedPreferences.getString("fonction", "N/A");
        String role = sharedPreferences.getString("role", "N/A");

        tvNom.setText(nom);
        tvPrenom.setText(prenom);
        tvEmail.setText(email);
        tvFonction.setText(fonction.replace("_", " "));
        tvRole.setText(role);
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Déconnexion")
            .setMessage("Voulez-vous vraiment vous déconnecter ?")
            .setPositiveButton("Oui", (dialog, which) -> logout())
            .setNegativeButton("Non", null)
            .show();
    }

    private void logout() {
        // Effacer la session
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // ✅ Retour à la page d'accueil (RoleSelectionActivity)
        Intent intent = new Intent(ProfilAdminActivity.this, RoleSelectionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}