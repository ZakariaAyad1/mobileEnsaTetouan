/*salma*/
package com.example.ensatecertnotes.ui.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.DatabaseHelper;
import com.example.ensatecertnotes.db.dao.EtudiantDao_etudiant;
import com.example.ensatecertnotes.model.Etudiant;
import com.example.ensatecertnotes.model.User;
import com.example.ensatecertnotes.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

public class ProfilFragment_etudiant extends Fragment {

    private TextView tvHeaderName, tvCne, tvNom, tvEmail, tvFiliere, tvAnneeEtude; // Removed tvPrenom, Added tvHeaderName
    private MaterialButton btnLogout;
    private SessionManager sessionManager;
    private EtudiantDao_etudiant etudiantDao;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_etudiant, container, false);

        // Initialize dependencies
        sessionManager = new SessionManager(requireContext());
        dbHelper = DatabaseHelper.getInstance(requireContext());
        etudiantDao = new EtudiantDao_etudiant(dbHelper);

        // Initialize views
        tvHeaderName = view.findViewById(R.id.tv_nom_prenom_header); // New Header TextView
        tvCne = view.findViewById(R.id.tv_cne_profil);
        tvNom = view.findViewById(R.id.tv_nom_profil); // Now displays Full Name
        // tvPrenom = view.findViewById(R.id.tv_prenom_profil); // Removed
        tvEmail = view.findViewById(R.id.tv_email_profil);
        tvFiliere = view.findViewById(R.id.tv_filiere_profil);
        tvAnneeEtude = view.findViewById(R.id.tv_annee_etude_profil);
        btnLogout = view.findViewById(R.id.btn_logout_profil);

        loadProfileData();

        // Logout listener
        btnLogout.setOnClickListener(v -> {
            sessionManager.logoutUser();
        });

        return view;
    }

    private void loadProfileData() {
        int userId = sessionManager.getUserId();

        // Retrieve student data
        Etudiant etudiant = etudiantDao.getEtudiantByUserId(userId);
        
        if (etudiant != null) {
            String fullName = etudiant.getNom() + " " + etudiant.getPrenom();

            if (tvHeaderName != null) {
                tvHeaderName.setText(fullName);
            }
            
            tvCne.setText(etudiant.getCne());
            tvNom.setText(fullName); // Set Full Name
            // tvPrenom.setText(etudiant.getPrenom()); // Removed
            tvFiliere.setText(etudiant.getFiliere());
            tvAnneeEtude.setText(etudiant.getAnneeEtude());
            
            // Get email from User table
             User user = getUserById(userId);
             if (user != null) {
                 tvEmail.setText(user.getEmail());
             }
        }
    }

    // Helper to get User email
    private User getUserById(int userId) {
        android.database.Cursor cursor = dbHelper.getReadableDatabase()
            .rawQuery("SELECT * FROM users WHERE id = ?", 
                new String[]{String.valueOf(userId)});
        
        User user = null;
        if (cursor.moveToFirst()) {
            user = new User();
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        }
        cursor.close();
        return user;
    }
}
/*salma*/
