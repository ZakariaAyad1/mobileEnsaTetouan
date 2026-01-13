package com.example.ensatecertnotes.ui.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.DatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardAdminActivity extends AppCompatActivity {

    private TextView tvWelcome, tvDemandesEnAttente, tvDemandesValidees, tvDemandesRejetees, tvTotalEtudiants;
    private CardView cardDemandes, cardEtudiants;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        dbHelper = DatabaseHelper.getInstance(this);
        sharedPreferences = getSharedPreferences("AdminSession", MODE_PRIVATE);

        // Initialisation des vues
        tvWelcome = findViewById(R.id.tv_welcome_admin);
        tvDemandesEnAttente = findViewById(R.id.tv_demandes_en_attente);
        tvDemandesValidees = findViewById(R.id.tv_demandes_validees);
        tvDemandesRejetees = findViewById(R.id.tv_demandes_rejetees);
        tvTotalEtudiants = findViewById(R.id.tv_total_etudiants);
        cardDemandes = findViewById(R.id.card_demandes);
        cardEtudiants = findViewById(R.id.card_etudiants);
        bottomNav = findViewById(R.id.bottom_nav_admin);

        // Afficher nom admin
        String prenom = sharedPreferences.getString("prenom", "");
        String nom = sharedPreferences.getString("nom", "");
        tvWelcome.setText("Bienvenue, " + prenom + " " + nom);

        // Charger les statistiques
        loadStatistics();

        // Navigation vers liste demandes
        cardDemandes.setOnClickListener(v -> {
            startActivity(new Intent(this, ListeDemandesActivity.class));
        });

        // Navigation vers gestion étudiants
        cardEtudiants.setOnClickListener(v -> {
            startActivity(new Intent(this, GestionEtudiantsActivity.class));
        });

        // Bottom Navigation
        bottomNav.setSelectedItemId(R.id.nav_dashboard);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_dashboard) {
                return true;
            } else if (itemId == R.id.nav_demandes) {
                startActivity(new Intent(this, ListeDemandesActivity.class));
                return true;
            } else if (itemId == R.id.nav_etudiants) {
                startActivity(new Intent(this, GestionEtudiantsActivity.class));
                return true;
            } else if (itemId == R.id.nav_profil) {
                startActivity(new Intent(this, ProfilAdminActivity.class));
                return true;
            }
            return false;
        });
    }

    private void loadStatistics() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Demandes en attente
        Cursor cursorEnAttente = db.rawQuery(
            "SELECT COUNT(*) FROM demandes_certificats WHERE statut = 'EN_ATTENTE' " +
            "UNION ALL SELECT COUNT(*) FROM demandes_diplomes WHERE statut = 'EN_ATTENTE'", null);
        int enAttente = 0;
        while (cursorEnAttente.moveToNext()) {
            enAttente += cursorEnAttente.getInt(0);
        }
        tvDemandesEnAttente.setText(String.valueOf(enAttente));
        cursorEnAttente.close();

        // Demandes validées
        Cursor cursorValidees = db.rawQuery(
            "SELECT COUNT(*) FROM demandes_certificats WHERE statut = 'VALIDE' " +
            "UNION ALL SELECT COUNT(*) FROM demandes_diplomes WHERE statut = 'VALIDE'", null);
        int validees = 0;
        while (cursorValidees.moveToNext()) {
            validees += cursorValidees.getInt(0);
        }
        tvDemandesValidees.setText(String.valueOf(validees));
        cursorValidees.close();

        // Demandes rejetées
        Cursor cursorRejetees = db.rawQuery(
            "SELECT COUNT(*) FROM demandes_certificats WHERE statut = 'REJETE' " +
            "UNION ALL SELECT COUNT(*) FROM demandes_diplomes WHERE statut = 'REJETE'", null);
        int rejetees = 0;
        while (cursorRejetees.moveToNext()) {
            rejetees += cursorRejetees.getInt(0);
        }
        tvDemandesRejetees.setText(String.valueOf(rejetees));
        cursorRejetees.close();

        // Total étudiants
        Cursor cursorEtudiants = db.rawQuery("SELECT COUNT(*) FROM etudiants", null);
        if (cursorEtudiants.moveToFirst()) {
            tvTotalEtudiants.setText(String.valueOf(cursorEtudiants.getInt(0)));
        }
        cursorEtudiants.close();

        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStatistics(); // Recharger les stats au retour
    }
}