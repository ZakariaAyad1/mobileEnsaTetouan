/*salma*/
package com.example.ensatecertnotes.ui.student;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.DatabaseHelper;
import com.example.ensatecertnotes.db.dao.EtudiantDao_etudiant;
import com.example.ensatecertnotes.model.Etudiant;
import com.example.ensatecertnotes.utils.SessionManager;

public class ProfilEtudiantActivity_etudiant extends AppCompatActivity {
    
    private TextView tvCne;
    private TextView tvNom;
    private TextView tvPrenom;
    private TextView tvEmail;
    private TextView tvFiliere;
    private TextView tvAnneeEtude;
    private Button btnLogout;
    
    private DatabaseHelper dbHelper;
    private EtudiantDao_etudiant etudiantDao;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_etudiant);

        // Initialize database and session
        dbHelper = DatabaseHelper.getInstance(this);
        etudiantDao = new EtudiantDao_etudiant(dbHelper);
        sessionManager = new SessionManager(this);

        // Set up toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mon Profil");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        tvCne = findViewById(R.id.tv_cne);
        tvNom = findViewById(R.id.tv_nom);
        tvPrenom = findViewById(R.id.tv_prenom);
        tvEmail = findViewById(R.id.tv_email);
        tvFiliere = findViewById(R.id.tv_filiere);
        tvAnneeEtude = findViewById(R.id.tv_annee_etude);
        btnLogout = findViewById(R.id.btn_logout);

        // Load student data
        loadStudentProfile();

        // Set logout button listener
        btnLogout.setOnClickListener(v -> {
            sessionManager.logoutUser();
            finish();
        });
    }

    private void loadStudentProfile() {
        int etudiantId = sessionManager.getEtudiantId();
        Etudiant etudiant = etudiantDao.getEtudiantById(etudiantId);

        if (etudiant != null) {
            tvCne.setText(etudiant.getCne());
            tvNom.setText(etudiant.getNom());
            tvPrenom.setText(etudiant.getPrenom());
            tvFiliere.setText(etudiant.getFiliere());
            tvAnneeEtude.setText(etudiant.getAnneeEtude());

            // Get email from user table
            loadUserEmail(etudiant.getUserId());
        }
    }

    private void loadUserEmail(int userId) {
        android.database.Cursor cursor = dbHelper.getReadableDatabase()
            .rawQuery("SELECT email FROM users WHERE id = ?", 
                new String[]{String.valueOf(userId)});
        
        if (cursor.moveToFirst()) {
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            tvEmail.setText(email);
        }
        cursor.close();
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
