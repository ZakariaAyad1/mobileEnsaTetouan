/*salma*/
package com.example.ensatecertnotes.ui.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.utils.SessionManager;

public class HomeFragment_etudiant extends Fragment {

    private CardView cardResultats;
    private CardView cardDiplome;
    private CardView cardCertificats;
    private TextView tvWelcome;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_etudiant, container, false);

        // Initialize session manager
        sessionManager = new SessionManager(requireContext());

        // Initialize views
        tvWelcome = view.findViewById(R.id.tv_welcome_home);
        cardResultats = view.findViewById(R.id.card_resultats_home);
        cardDiplome = view.findViewById(R.id.card_diplome_home);
        cardCertificats = view.findViewById(R.id.card_certificats_home);

        // Set welcome message
        String userName = sessionManager.getUserName();
        if (userName != null) {
            tvWelcome.setText("Bienvenue, " + userName);
        }

        // Set click listeners
        cardResultats.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ConsultationNotesActivity_etudiant.class);
            startActivity(intent);
        });

        cardDiplome.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), DemandeDiplomeActivity_etudiant.class);
            startActivity(intent);
        });

        cardCertificats.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), DemandeCertificatActivity_etudiant.class);
            startActivity(intent);
        });

        return view;
    }
}
/*salma*/
