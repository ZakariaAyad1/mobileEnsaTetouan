/*salma*/
package com.example.ensatecertnotes.ui.student;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.DatabaseHelper;
import com.example.ensatecertnotes.db.dao.DiplomaDao_etudiant;
import com.example.ensatecertnotes.model.DemandeDiplome_etudiant;
import com.example.ensatecertnotes.utils.SessionManager;

public class DemandeDiplomeActivity_etudiant extends AppCompatActivity {
    
    private EditText etAdresse;
    private EditText etTelephone;
    private EditText etCommentaire;
    private Button btnSubmit;
    private LinearLayout tvStatutSection;
    private TextView tvStatut;
    private TextView tvDateDemande;
    
    private DatabaseHelper dbHelper;
    private DiplomaDao_etudiant diplomaDao;
    private SessionManager sessionManager;
    private DemandeDiplome_etudiant existingDemande;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demande_diplome_etudiant);

        // Initialize database and session
        dbHelper = DatabaseHelper.getInstance(this);
        diplomaDao = new DiplomaDao_etudiant(dbHelper);
        sessionManager = new SessionManager(this);

        // Set up toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Demande de Diplôme");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        etAdresse = findViewById(R.id.et_adresse);
        etTelephone = findViewById(R.id.et_telephone);
        etCommentaire = findViewById(R.id.et_commentaire);
        btnSubmit = findViewById(R.id.btn_submit);
        tvStatutSection = findViewById(R.id.tv_statut_section);
        tvStatut = findViewById(R.id.tv_statut);
        tvDateDemande = findViewById(R.id.tv_date_demande);

        // Check for existing request
        try {
            checkExistingDemande();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur lors du chargement: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Set up button listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (existingDemande == null) {
                    submitRequest();
                } else {
                    updateRequest();
                }
            }
        });
    }

    private void checkExistingDemande() {
        int etudiantId = sessionManager.getEtudiantId();
        existingDemande = diplomaDao.getDemandeDiplomeByEtudiant(etudiantId);

        if (existingDemande != null) {
            // Show existing request
            tvStatutSection.setVisibility(View.VISIBLE);
            tvStatut.setText("Statut: " + formatStatut(existingDemande.getStatut()));
            tvDateDemande.setText("Date de demande: " + existingDemande.getDateDemande());

            // Fill form with existing data
            if (existingDemande.getAdresseLivraison() != null) {
                etAdresse.setText(existingDemande.getAdresseLivraison());
            }
            if (existingDemande.getTelephone() != null) {
                etTelephone.setText(existingDemande.getTelephone());
            }
            if (existingDemande.getCommentaire() != null) {
                etCommentaire.setText(existingDemande.getCommentaire());
            }

            // Change button text
            btnSubmit.setText("Modifier la demande");

            // Disable editing if request is already processed
            if (!"EN_ATTENTE".equals(existingDemande.getStatut())) {
                etAdresse.setEnabled(false);
                etTelephone.setEnabled(false);
                etCommentaire.setEnabled(false);
                btnSubmit.setEnabled(false);
            }
        } else {
            tvStatutSection.setVisibility(View.GONE);
        }
    }

    private void submitRequest() {
        String adresse = etAdresse.getText().toString().trim();
        String telephone = etTelephone.getText().toString().trim();
        String commentaire = etCommentaire.getText().toString().trim();

        if (adresse.isEmpty() || telephone.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir l'adresse et le téléphone", Toast.LENGTH_SHORT).show();
            return;
        }

        DemandeDiplome_etudiant diplome = new DemandeDiplome_etudiant();
        diplome.setEtudiantId(sessionManager.getEtudiantId());
        diplome.setAdresseLivraison(adresse);
        diplome.setTelephone(telephone);
        diplome.setCommentaire(commentaire);

        long result = diplomaDao.createDemandeDiplome(diplome);

        if (result > 0) {
            Toast.makeText(this, "Demande de diplôme envoyée avec succès", Toast.LENGTH_LONG).show();
            checkExistingDemande(); // Refresh the view
        } else {
            Toast.makeText(this, "Erreur lors de l'envoi de la demande", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateRequest() {
        String adresse = etAdresse.getText().toString().trim();
        String telephone = etTelephone.getText().toString().trim();
        String commentaire = etCommentaire.getText().toString().trim();

        if (adresse.isEmpty() || telephone.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir l'adresse et le téléphone", Toast.LENGTH_SHORT).show();
            return;
        }

        existingDemande.setAdresseLivraison(adresse);
        existingDemande.setTelephone(telephone);
        existingDemande.setCommentaire(commentaire);

        boolean result = diplomaDao.updateDemandeDiplome(existingDemande);

        if (result) {
            Toast.makeText(this, "Demande mise à jour avec succès", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
        }
    }

    private String formatStatut(String statut) {
        switch (statut) {
            case "EN_ATTENTE":
                return "En attente";
            case "APPROUVE":
                return "Approuvé";
            case "REJETE":
                return "Rejeté";
            case "PRET":
                return "Prêt";
            default:
                return statut;
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
