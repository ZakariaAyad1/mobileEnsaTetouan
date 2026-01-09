/*salma*/
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
import com.example.ensatecertnotes.db.dao.NoteDao_etudiant;
import com.example.ensatecertnotes.ui.adapters.NoteAdapter_etudiant;
import android.widget.TextView;
import java.util.List;

/**
 * Fragment to display grades for a specific semester
 */
public class SemestreFragment_etudiant extends Fragment {
    
    private static final String ARG_ETUDIANT_ID = "etudiant_id";
    private static final String ARG_SEMESTRE = "semestre";
    
    private RecyclerView recyclerView;
    private android.widget.TextView tvMoyenne; // Add field
    private NoteAdapter_etudiant adapter;
    
    public static SemestreFragment_etudiant newInstance(int etudiantId, int semestre) {
        SemestreFragment_etudiant fragment = new SemestreFragment_etudiant();
        Bundle args = new Bundle();
        args.putInt(ARG_ETUDIANT_ID, etudiantId);
        args.putInt(ARG_SEMESTRE, semestre);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_semestre_etudiant, container, false);
        
        recyclerView = view.findViewById(R.id.recycler_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tvMoyenne = view.findViewById(R.id.tv_moyenne_semestre); // Initialize here
        
        if (getArguments() != null) {
            int etudiantId = getArguments().getInt(ARG_ETUDIANT_ID);
            int semestre = getArguments().getInt(ARG_SEMESTRE);
            loadNotes(etudiantId, semestre);
        }
        
        return view;
    }

    private void loadNotes(int etudiantId, int semestre) {
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(getContext());
        NoteDao_etudiant noteDao = new NoteDao_etudiant(dbHelper);
        
        List<NoteDao_etudiant.NoteWithModule> notes = noteDao.getNotesBySemestre(etudiantId, semestre);
        
        // Calculate average
        double totalPoints = 0;
        double totalCoeffs = 0;
        
        for (NoteDao_etudiant.NoteWithModule note : notes) {
            // Note finale is inside the nested Note object
            if (note.getNote().getNoteFinale() != null) {
                // Using credits as coefficient
                totalPoints += note.getNote().getNoteFinale() * note.getCredits();
                totalCoeffs += note.getCredits();
            }
        }
        
        String averageStr = "--/20";
        if (totalCoeffs > 0) {
            double average = totalPoints / totalCoeffs;
            averageStr = String.format(java.util.Locale.getDefault(), "Moyenne: %.2f/20", average);
        }
        
        // Use the field directly
        if (tvMoyenne != null) {
            tvMoyenne.setText(averageStr);
        }
        
        adapter = new NoteAdapter_etudiant(getContext(), notes);
        recyclerView.setAdapter(adapter);
    }
}
/*salma*/
