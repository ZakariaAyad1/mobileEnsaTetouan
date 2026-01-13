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
    private com.example.ensatecertnotes.db.dao.InscriptionDao inscriptionDao;

    public EtudiantAdapter(Context context, List<Etudiant> etudiantList, int moduleId, String moduleName) {
        this.context = context;
        this.etudiantList = etudiantList;
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.inscriptionDao = new com.example.ensatecertnotes.db.dao.InscriptionDao(context);
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

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, com.example.ensatecertnotes.ui.prof.AddStudentActivity.class);
            intent.putExtra("STUDENT_ID", etudiant.getId());
            context.startActivity(intent);
        });

        holder.btnUnenroll.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(context)
                    .setTitle("Désinscrire l'étudiant")
                    .setMessage("Voulez-vous vraiment retirer cet étudiant de ce module ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        int res = inscriptionDao.unenrollStudent(etudiant.getId(), moduleId);
                        if (res > 0) {
                            etudiantList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, etudiantList.size());
                            android.widget.Toast
                                    .makeText(context, "Étudiant retiré.", android.widget.Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Non", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return etudiantList.size();
    }

    public static class EtudiantViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCne, tvStatus;
        android.widget.ImageView btnUnenroll, btnEdit;

        public EtudiantViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_etudiant_name);
            tvCne = itemView.findViewById(R.id.tv_etudiant_cne);
            tvStatus = itemView.findViewById(R.id.tv_status_grade);
            btnUnenroll = itemView.findViewById(R.id.btn_unenroll);
            btnEdit = itemView.findViewById(R.id.btn_edit_etudiant);
        }
    }
}
