package com.example.ensatecertnotes.ui.admin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ensatecertnotes.R;
import java.util.ArrayList;
import java.util.HashMap;

public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.ViewHolder> {

    private Context context;
    private ArrayList<HashMap<String, String>> etudiantsList;
    private OnEtudiantClickListener listener;

    public interface OnEtudiantClickListener {
        void onEtudiantClick(HashMap<String, String> etudiant);
    }

    public EtudiantAdapter(Context context, ArrayList<HashMap<String, String>> etudiantsList, OnEtudiantClickListener listener) {
        this.context = context;
        this.etudiantsList = etudiantsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_etudiant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, String> etudiant = etudiantsList.get(position);

        holder.tvCNE.setText("CNE: " + etudiant.get("cne"));
        holder.tvNom.setText(etudiant.get("nom") + " " + etudiant.get("prenom"));
        holder.tvFiliere.setText(etudiant.get("filiere") + " - " + etudiant.get("annee"));
        holder.tvEmail.setText(etudiant.get("email"));
        
        String moyenne = etudiant.get("moyenne");
        holder.tvMoyenne.setText("Moyenne: " + moyenne + " / 20");
        
        // Couleur selon la moyenne
        try {
            double moy = Double.parseDouble(moyenne);
            if (moy >= 16) {
                holder.tvMoyenne.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            } else if (moy >= 12) {
                holder.tvMoyenne.setTextColor(context.getResources().getColor(android.R.color.holo_blue_dark));
            } else if (moy >= 10) {
                holder.tvMoyenne.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
            } else {
                holder.tvMoyenne.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            }
        } catch (Exception e) {
            holder.tvMoyenne.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEtudiantClick(etudiant);
            }
        });
    }

    @Override
    public int getItemCount() {
        return etudiantsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCNE, tvNom, tvFiliere, tvEmail, tvMoyenne;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCNE = itemView.findViewById(R.id.tv_etudiant_cne);
            tvNom = itemView.findViewById(R.id.tv_etudiant_nom);
            tvFiliere = itemView.findViewById(R.id.tv_etudiant_filiere);
            tvEmail = itemView.findViewById(R.id.tv_etudiant_email);
            tvMoyenne = itemView.findViewById(R.id.tv_etudiant_moyenne);
        }
    }
}

/**
 * UTILISATION dans GestionEtudiantsActivity:
 * 
 * adapter = new EtudiantAdapter(this, etudiantsList, this::showEtudiantDetails);
 * recyclerView.setAdapter(adapter);
 * 
 * FONCTIONNALITÉS:
 * - Affiche la liste des étudiants
 * - Colore la moyenne selon la performance
 * - Gère le clic sur un étudiant pour afficher ses détails
 * - Compatible avec le layout item_etudiant.xml
 */