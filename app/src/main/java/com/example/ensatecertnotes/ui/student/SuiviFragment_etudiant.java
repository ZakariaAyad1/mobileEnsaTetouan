
package com.example.ensatecertnotes.ui.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.DatabaseHelper;
import com.example.ensatecertnotes.db.dao.CertificatDao_etudiant;
import com.example.ensatecertnotes.model.Certificat;
import com.example.ensatecertnotes.ui.adapters.DemandeAdapter_etudiant;
import com.example.ensatecertnotes.utils.SessionManager;
import java.util.List;
import java.util.ArrayList;

public class SuiviFragment_etudiant extends Fragment {

    private RecyclerView recyclerView;
    private DemandeAdapter_etudiant adapter;
    private CertificatDao_etudiant certificatDao;
    private SessionManager sessionManager;
    private int etudiantId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suivi_etudiant, container, false);

        // Initialize
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(requireContext());
        certificatDao = new CertificatDao_etudiant(dbHelper);
        sessionManager = new SessionManager(requireContext());
        etudiantId = sessionManager.getEtudiantId();

        // Setup RecyclerView
        recyclerView = view.findViewById(R.id.recycler_demandes_suivi);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        loadDemandes();

        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        loadDemandes();
    }

    private void loadDemandes() {
        // Load certificate requests
        List<Certificat> demandes = certificatDao.getDemandesByEtudiant(etudiantId);
        if (demandes == null) {
            demandes = new ArrayList<>();
        }

        // Load diploma request
        try {
            com.example.ensatecertnotes.db.dao.DiplomaDao_etudiant diplomaDao = 
                new com.example.ensatecertnotes.db.dao.DiplomaDao_etudiant(
                    DatabaseHelper.getInstance(requireContext()));
            
            com.example.ensatecertnotes.model.DemandeDiplome_etudiant diplome = 
                diplomaDao.getDemandeDiplomeByEtudiant(etudiantId);

            // If exists, add to list as a Certificat object for display
            if (diplome != null) {
                Certificat certDiplome = new Certificat();
                certDiplome.setId(diplome.getId()); 
                certDiplome.setEtudiantId(diplome.getEtudiantId());
                certDiplome.setType("DIPLOME");
                certDiplome.setStatut(diplome.getStatut());
                certDiplome.setDateDemande(diplome.getDateDemande());
                demandes.add(0, certDiplome); // Add direct to top
            }
        } catch (Exception e) {
            e.printStackTrace();
            android.util.Log.e("SuiviFragment", "Error loading diploma: " + e.getMessage());
        }

        adapter = new DemandeAdapter_etudiant(requireContext(), demandes);
        recyclerView.setAdapter(adapter);
    }
}

