/*salma*/
package com.example.ensatecertnotes.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.DatabaseHelper;
import com.example.ensatecertnotes.db.dao.CertificatDao_etudiant;
import com.example.ensatecertnotes.db.dao.NotificationDao_etudiant; // salma
import com.example.ensatecertnotes.model.Certificat;
import com.example.ensatecertnotes.utils.SessionManager;

public class DemandeCertificatActivity_etudiant extends AppCompatActivity {
    
    private Spinner spinnerType;
    private EditText etMotif;
    private Button btnSubmit;
    
    private DatabaseHelper dbHelper;
    private CertificatDao_etudiant certificatDao;
    /*salma*/
    private NotificationDao_etudiant notificationDao;
    /*salma*/
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demande_certificat_etudiant);

        // Initialize database and session
        dbHelper = DatabaseHelper.getInstance(this);
        certificatDao = new CertificatDao_etudiant(dbHelper);
        /*salma*/
        notificationDao = new NotificationDao_etudiant(this);
        /*salma*/
        sessionManager = new SessionManager(this);

        // Set up toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Demande de Certificat");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        spinnerType = findViewById(R.id.spinner_certificat_type);
        etMotif = findViewById(R.id.et_motif);
        btnSubmit = findViewById(R.id.btn_submit);

        // Set up spinner with all certificate types from database
        String[] typesDisplay = {
            "Certificat de Scolarité",
            "Attestation de Réussite", 
            "Relevé de Notes",
            "Attestation d'Inscription"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, typesDisplay);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        // Set up button listeners
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRequest();
            }
        });


    }

    private void submitRequest() {
        String motif = etMotif.getText().toString().trim();
        
        if (motif.isEmpty()) {
            Toast.makeText(this, "Veuillez saisir le motif de la demande", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected type and map to database value
        int selectedPosition = spinnerType.getSelectedItemPosition();
        String type;
        switch (selectedPosition) {
            case 0:
                type = "CERTIFICAT_SCOLARITE";
                break;
            case 1:
                type = "ATTESTATION_REUSSITE";
                break;
            case 2:
                type = "RELEVE_NOTES";
                break;
            case 3:
                type = "ATTESTATION_INSCRIPTION";
                break;
            default:
                type = "CERTIFICAT_SCOLARITE";
        }

        // Create certificate request
        Certificat certificat = new Certificat();
        certificat.setEtudiantId(sessionManager.getEtudiantId());
        certificat.setType(type);
        certificat.setMotif(motif);

        long result = certificatDao.createDemandeCertificat(certificat);

        if (result > 0) {
            /*salma*/
            notificationDao.notifyAdmins("Nouvelle Demande Certificat", "Un étudiant a demandé un " + type, "INFO");
            /*salma*/
            Toast.makeText(this, "Demande envoyée avec succès", Toast.LENGTH_SHORT).show();
            // Clear form
            etMotif.setText("");
            spinnerType.setSelection(0);
            
            // Navigate to tracking page
            Intent intent = new Intent(this, SuiviDemandesActivity_etudiant.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Erreur lors de l'envoi de la demande", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
/*salma*/
