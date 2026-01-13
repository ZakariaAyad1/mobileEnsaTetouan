package com.example.ensatecertnotes.ui.admin;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.DatabaseHelper;
import com.example.ensatecertnotes.ui.admin.adapters.DemandeAdapter;
import com.example.ensatecertnotes.db.dao.NotificationDao_etudiant; // salma
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.HashMap;

public class ListeDemandesActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private Spinner spinnerFilter;
    private DemandeAdapter adapter;
    private DatabaseHelper dbHelper;
    private BottomNavigationView bottomNav;
    private ArrayList<HashMap<String, String>> demandesList;
    private String currentTab = "CERTIFICATS"; // CERTIFICATS ou DIPLOMES

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_demandes);

        dbHelper = DatabaseHelper.getInstance(this);
        demandesList = new ArrayList<>();

        // Initialisation
        tabLayout = findViewById(R.id.tab_layout_demandes);
        recyclerView = findViewById(R.id.recycler_demandes);
        spinnerFilter = findViewById(R.id.spinner_filter_statut);
        bottomNav = findViewById(R.id.bottom_nav_admin);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DemandeAdapter(this, demandesList, this::onDemandeAction);
        recyclerView.setAdapter(adapter);

        // Setup Spinner (filtre par statut)
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.statut_filter_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(spinnerAdapter);
        spinnerFilter.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                loadDemandes();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        // Setup Tabs
        tabLayout.addTab(tabLayout.newTab().setText("Certificats"));
        tabLayout.addTab(tabLayout.newTab().setText("Diplômes"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition() == 0 ? "CERTIFICATS" : "DIPLOMES";
                loadDemandes();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Bottom Navigation
        bottomNav.setSelectedItemId(R.id.nav_demandes);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_dashboard) {
                startActivity(new Intent(this, DashboardAdminActivity.class));
                return true;
            } else if (itemId == R.id.nav_demandes) {
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

        // Charger les demandes
        loadDemandes();
    }

    private void loadDemandes() {
        demandesList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String filterStatut = spinnerFilter.getSelectedItem().toString();
        String statutCondition = "";
        if (!filterStatut.equals("TOUS")) {
            statutCondition = " AND statut = '" + filterStatut + "'";
        }

        if (currentTab.equals("CERTIFICATS")) {
            String query = "SELECT dc.id, e.cne, e.nom, e.prenom, dc.type_certificat, dc.motif, dc.statut, dc.date_demande " +
                          "FROM demandes_certificats dc " +
                          "INNER JOIN etudiants e ON dc.etudiant_id = e.id " +
                          "WHERE 1=1" + statutCondition + " ORDER BY dc.date_demande DESC";
            
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                HashMap<String, String> demande = new HashMap<>();
                demande.put("id", cursor.getString(0));
                demande.put("type", "CERTIFICAT");
                demande.put("cne", cursor.getString(1));
                demande.put("nom", cursor.getString(2) + " " + cursor.getString(3));
                demande.put("type_certificat", cursor.getString(4));
                demande.put("motif", cursor.getString(5));
                demande.put("statut", cursor.getString(6));
                demande.put("date", cursor.getString(7));
                demandesList.add(demande);
            }
            cursor.close();
        } else {
            String query = "SELECT dd.id, e.cne, e.nom, e.prenom, dd.adresse_livraison, dd.telephone, dd.statut, dd.date_demande " +
                          "FROM demandes_diplomes dd " +
                          "INNER JOIN etudiants e ON dd.etudiant_id = e.id " +
                          "WHERE 1=1" + statutCondition + " ORDER BY dd.date_demande DESC";
            
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                HashMap<String, String> demande = new HashMap<>();
                demande.put("id", cursor.getString(0));
                demande.put("type", "DIPLOME");
                demande.put("cne", cursor.getString(1));
                demande.put("nom", cursor.getString(2) + " " + cursor.getString(3));
                demande.put("adresse", cursor.getString(4));
                demande.put("telephone", cursor.getString(5));
                demande.put("statut", cursor.getString(6));
                demande.put("date", cursor.getString(7));
                demandesList.add(demande);
            }
            cursor.close();
        }

        db.close();
        adapter.notifyDataSetChanged();
    }

    private void onDemandeAction(String action, String id, String type) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        /*salma*/
        NotificationDao_etudiant notificationDao = new NotificationDao_etudiant(this);
        int etudiantId = -1;
        String tableName = type.equals("CERTIFICAT") ? "demandes_certificats" : "demandes_diplomes";
        
        Cursor cursor = db.rawQuery("SELECT etudiant_id FROM " + tableName + " WHERE id = ?", new String[]{id});
        if (cursor.moveToFirst()) {
            etudiantId = cursor.getInt(0);
        }
        cursor.close();
        /*salma*/
        
        if (action.equals("VALIDER")) {
            if (type.equals("CERTIFICAT")) {
                db.execSQL("UPDATE demandes_certificats SET statut = 'VALIDE', date_traitement = datetime('now') WHERE id = ?", new String[]{id});
            } else {
                db.execSQL("UPDATE demandes_diplomes SET statut = 'VALIDE', date_traitement = datetime('now') WHERE id = ?", new String[]{id});
            }
            /*salma*/
            if (etudiantId != -1) {
                notificationDao.notifyStudent(etudiantId, "Demande Validée", "Votre demande de " + type.toLowerCase() + " a été validée.", "SUCCES");
            }
            /*salma*/
            android.widget.Toast.makeText(this, "Demande validée", android.widget.Toast.LENGTH_SHORT).show();
        } else if (action.equals("REJETER")) {
            if (type.equals("CERTIFICAT")) {
                db.execSQL("UPDATE demandes_certificats SET statut = 'REJETE', date_traitement = datetime('now') WHERE id = ?", new String[]{id});
            } else {
                db.execSQL("UPDATE demandes_diplomes SET statut = 'REJETE', date_traitement = datetime('now') WHERE id = ?", new String[]{id});
            }
            /*salma*/
            if (etudiantId != -1) {
                notificationDao.notifyStudent(etudiantId, "Demande Rejetée", "Votre demande de " + type.toLowerCase() + " a été rejetée.", "ALERTE");
            }
            /*salma*/
            android.widget.Toast.makeText(this, "Demande rejetée", android.widget.Toast.LENGTH_SHORT).show();
        }
        
        db.close();
        loadDemandes(); // Recharger la liste
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDemandes();
    }
}