package com.example.ensatecertnotes.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.model.Etudiant;
import com.example.ensatecertnotes.ui.prof.SaisieNoteActivity;

import java.util.List;

public class EtudiantAdapter extends RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder> {

    private Context context;
    private List<Etudiant> etudiantList;
    private int moduleId;
    private String moduleName;

    public EtudiantAdapter(Context context, List<Etudiant> etudiantList, int moduleId, String moduleName) {
        this.context = context;
        this.etudiantList = etudiantList;
        this.moduleId = moduleId;
        this.moduleName = moduleName;
    }

    @NonNull
    @Override
    public EtudiantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_etudiant_list, parent, false);
        return new EtudiantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EtudiantViewHolder holder, int position) {
        Etudiant etudiant = etudiantList.get(position);
        holder.tvName.setText(etudiant.getNom() + " " + etudiant.getPrenom());
        holder.tvCne.setText("CNE: " + etudiant.getCne());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SaisieNoteActivity.class);
            intent.putExtra("ETUDIANT_ID", etudiant.getId());
            intent.putExtra("MODULE_ID", moduleId);
            intent.putExtra("ETUDIANT_NAME", etudiant.getNom() + " " + etudiant.getPrenom());
            intent.putExtra("MODULE_NAME", moduleName);
            intent.putExtra("ETUDIANT_CNE", etudiant.getCne());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return etudiantList.size();
    }

    public static class EtudiantViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCne, tvStatus;

        public EtudiantViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_etudiant_name);
            tvCne = itemView.findViewById(R.id.tv_etudiant_cne);
            tvStatus = itemView.findViewById(R.id.tv_status_grade);
        }
    }
}
