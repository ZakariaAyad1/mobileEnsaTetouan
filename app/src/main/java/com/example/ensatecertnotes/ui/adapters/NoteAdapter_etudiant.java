/*salma*/
package com.example.ensatecertnotes.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.dao.NoteDao_etudiant;
import com.example.ensatecertnotes.ui.student.DetailModuleActivity_etudiant;
import java.util.List;

/**
 * Adapter for displaying grades in RecyclerView
 */
public class NoteAdapter_etudiant extends RecyclerView.Adapter<NoteAdapter_etudiant.NoteViewHolder> {
    
    private Context context;
    private List<NoteDao_etudiant.NoteWithModule> notes;

    public NoteAdapter_etudiant(Context context, List<NoteDao_etudiant.NoteWithModule> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note_semestre_etudiant, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        NoteDao_etudiant.NoteWithModule noteWithModule = notes.get(position);
        
        holder.tvModuleCode.setText(noteWithModule.getModuleCode());
        holder.tvModuleName.setText(noteWithModule.getModuleNom());
        
        Double noteFinale = noteWithModule.getNote().getNoteFinale();
        if (noteFinale != null) {
            holder.tvNoteFinale.setText(String.format("%.2f / 20", noteFinale));
        } else {
            holder.tvNoteFinale.setText("-");
        }
        
        String statut = noteWithModule.getNote().getStatut();
        holder.tvStatut.setText(statut != null ? statut : "Non évalué");
        
        // Set status color
        if ("VALIDE".equals(statut)) {
            holder.tvStatut.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else if ("NON_VALIDE".equals(statut)) {
            holder.tvStatut.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else {
            holder.tvStatut.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
        }
        
        // Set click listener to open module details
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailModuleActivity_etudiant.class);
            intent.putExtra("MODULE_ID", noteWithModule.getNote().getModuleId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notes != null ? notes.size() : 0;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvModuleCode;
        TextView tvModuleName;
        TextView tvNoteFinale;
        TextView tvStatut;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_note);
            tvModuleCode = itemView.findViewById(R.id.tv_module_code);
            tvModuleName = itemView.findViewById(R.id.tv_module_name);
            tvNoteFinale = itemView.findViewById(R.id.tv_note_finale);
            tvStatut = itemView.findViewById(R.id.tv_statut);
        }
    }
}
/*salma*/
