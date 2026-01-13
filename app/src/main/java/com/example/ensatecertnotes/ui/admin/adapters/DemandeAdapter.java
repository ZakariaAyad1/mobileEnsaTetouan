package com.example.ensatecertnotes.ui.admin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ensatecertnotes.R;
import java.util.ArrayList;
import java.util.HashMap;

public class DemandeAdapter extends RecyclerView.Adapter<DemandeAdapter.ViewHolder> {

    private Context context;
    private ArrayList<HashMap<String, String>> demandesList;
    private OnDemandeActionListener listener;

    public interface OnDemandeActionListener {
        void onAction(String action, String id, String type);
    }

    public DemandeAdapter(Context context, ArrayList<HashMap<String, String>> demandesList, OnDemandeActionListener listener) {
        this.context = context;
        this.demandesList = demandesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_demande, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String, String> demande = demandesList.get(position);

        String type = demande.get("type");
        String id = demande.get("id");
        String cne = demande.get("cne");
        String nom = demande.get("nom");
        String statut = demande.get("statut");
        String date = demande.get("date");

        holder.tvCNE.setText("CNE: " + cne);
        holder.tvNom.setText(nom);
        holder.tvDate.setText("Date: " + date);
        holder.tvStatut.setText(statut);

        // Couleur du statut
        if (statut.equals("EN_ATTENTE")) {
            holder.tvStatut.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
        } else if (statut.equals("VALIDE")) {
            holder.tvStatut.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.tvStatut.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }

        // Type de demande
        if (type.equals("CERTIFICAT")) {
            String typeCert = demande.get("type_certificat");
            String motif = demande.get("motif");
            holder.tvType.setText("Certificat: " + typeCert.replace("_", " "));
            holder.tvDetails.setText("Motif: " + (motif != null ? motif : "Non spécifié"));
        } else {
            String adresse = demande.get("adresse");
            String tel = demande.get("telephone");
            holder.tvType.setText("Demande de Diplôme");
            holder.tvDetails.setText("Adresse: " + adresse + "\nTél: " + tel);
        }

        // Boutons d'action (visibles uniquement si EN_ATTENTE)
        if (statut.equals("EN_ATTENTE")) {
            holder.btnValider.setVisibility(View.VISIBLE);
            holder.btnRejeter.setVisibility(View.VISIBLE);

            holder.btnValider.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAction("VALIDER", id, type);
                }
            });

            holder.btnRejeter.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAction("REJETER", id, type);
                }
            });
        } else {
            holder.btnValider.setVisibility(View.GONE);
            holder.btnRejeter.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return demandesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCNE, tvNom, tvType, tvDetails, tvStatut, tvDate;
        Button btnValider, btnRejeter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCNE = itemView.findViewById(R.id.tv_demande_cne);
            tvNom = itemView.findViewById(R.id.tv_demande_nom);
            tvType = itemView.findViewById(R.id.tv_demande_type);
            tvDetails = itemView.findViewById(R.id.tv_demande_details);
            tvStatut = itemView.findViewById(R.id.tv_demande_statut);
            tvDate = itemView.findViewById(R.id.tv_demande_date);
            btnValider = itemView.findViewById(R.id.btn_valider);
            btnRejeter = itemView.findViewById(R.id.btn_rejeter);
        }
    }
}