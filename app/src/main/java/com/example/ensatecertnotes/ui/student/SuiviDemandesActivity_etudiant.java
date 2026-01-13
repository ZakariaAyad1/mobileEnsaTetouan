/*salma*/
package com.example.ensatecertnotes.ui.student;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.DatabaseHelper;
import com.example.ensatecertnotes.db.dao.CertificatDao_etudiant;
import com.example.ensatecertnotes.model.Certificat;
import com.example.ensatecertnotes.ui.adapters.DemandeAdapter_etudiant;
import com.example.ensatecertnotes.utils.SessionManager;
import java.util.List;

public class SuiviDemandesActivity_etudiant extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private DemandeAdapter_etudiant adapter;
    
    private DatabaseHelper dbHelper;
    private CertificatDao_etudiant certificatDao;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suivi_demandes_etudiant);

        // Initialize database and session
        dbHelper = DatabaseHelper.getInstance(this);
        certificatDao = new CertificatDao_etudiant(dbHelper);
        sessionManager = new SessionManager(this);

        // Set up toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Suivi des Demandes");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_demandes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load data
        loadDemandes();
    }

    private void loadDemandes() {
        int etudiantId = sessionManager.getEtudiantId();
        List<Certificat> demandes = certificatDao.getDemandesByEtudiant(etudiantId);

        adapter = new DemandeAdapter_etudiant(this, demandes);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data when returning to this activity
        loadDemandes();
    }
}
/*salma*/
