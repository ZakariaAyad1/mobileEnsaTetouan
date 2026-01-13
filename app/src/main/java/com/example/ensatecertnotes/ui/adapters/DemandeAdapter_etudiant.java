
package com.example.ensatecertnotes.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.model.Certificat;
import java.util.List;

/**
 * Adapter for displaying certificate requests in RecyclerView
 */
public class DemandeAdapter_etudiant extends RecyclerView.Adapter<DemandeAdapter_etudiant.DemandeViewHolder> {
    
    private Context context;
    private List<Certificat> demandes;

    public DemandeAdapter_etudiant(Context context, List<Certificat> demandes) {
        this.context = context;
        this.demandes = demandes;
    }

    @NonNull
    @Override
    public DemandeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_demande_etudiant, parent, false);
        return new DemandeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DemandeViewHolder holder, int position) {
        Certificat demande = demandes.get(position);
        
        // Set type
        String type = formatType(demande.getType());
        holder.tvType.setText(type);
        
        // Set date
        String date = demande.getDateDemande();
        if (date != null && date.contains(" ")) {
            date = date.split(" ")[0]; // Show only date part
        } else if (date == null) {
            date = "Date inconnue";
        }
        holder.tvDate.setText("Demandé le: " + date);
        
        // Set status
        String statut = formatStatut(demande.getStatut());
        holder.tvStatut.setText(statut);
        
        // Set status color
        setStatutColor(holder.tvStatut, demande.getStatut());
    }

    @Override
    public int getItemCount() {
        return demandes != null ? demandes.size() : 0;
    }

    private String formatType(String type) {
        if (type == null) return "Type inconnu";
        switch (type) {
            case "CERTIFICAT_SCOLARITE":
                return "Certificat de Scolarité";
            case "ATTESTATION_REUSSITE":
                return "Attestation de Réussite";
            case "RELEVE_NOTES":
                return "Relevé de Notes";
            case "ATTESTATION_INSCRIPTION":
                return "Attestation d'Inscription";
            case "DIPLOME":
                return "Diplôme";
            // Legacy types for backward compatibility
            case "ATTESTATION_SCOLAIRE":
                return "Attestation Scolaire";
            case "ATTESTATION_STAGE":
                return "Attestation de Stage";
            default:
                return type;
        }
    }

    private String formatStatut(String statut) {
        if (statut == null) return "Statut inconnu";
        switch (statut) {
            case "EN_ATTENTE":
                return "En attente";
            case "APPROUVE":
                return "Approuvé";
            case "REJETE":
                return "Rejeté";
            case "DELIVRE":
                return "Délivré";
            default:
                return statut;
        }
    }

    private void setStatutColor(TextView textView, String statut) {
        int color;
        if ("APPROUVE".equals(statut) || "DELIVRE".equals(statut)) {
            color = context.getResources().getColor(android.R.color.holo_green_dark);
        } else if ("REJETE".equals(statut)) {
            color = context.getResources().getColor(android.R.color.holo_red_dark);
        } else if ("EN_ATTENTE".equals(statut)) {
            color = context.getResources().getColor(android.R.color.holo_orange_dark);
        } else {
            color = context.getResources().getColor(android.R.color.darker_gray);
        }
        textView.setTextColor(color);
    }

    static class DemandeViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvType;
        TextView tvDate;
        TextView tvStatut;

        public DemandeViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_demande);
            tvType = itemView.findViewById(R.id.tv_demande_type);
            tvDate = itemView.findViewById(R.id.tv_demande_date);
            tvStatut = itemView.findViewById(R.id.tv_demande_statut);
        }
    }
}

