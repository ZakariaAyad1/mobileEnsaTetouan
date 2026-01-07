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
import com.example.ensatecertnotes.model.Module;
import com.example.ensatecertnotes.ui.prof.ListeEtudiantsActivity;

import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder> {

    private Context context;
    private List<Module> moduleList;

    public ModuleAdapter(Context context, List<Module> moduleList) {
        this.context = context;
        this.moduleList = moduleList;
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_module_prof, parent, false);
        return new ModuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleViewHolder holder, int position) {
        Module module = moduleList.get(position);
        holder.tvName.setText(module.getNomModule());
        holder.tvCode.setText(module.getCodeModule());
        holder.tvSemestre.setText("Semestre " + module.getSemestre());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ListeEtudiantsActivity.class);
            intent.putExtra("MODULE_ID", module.getId());
            intent.putExtra("MODULE_NAME", module.getNomModule());
            context.startActivity(intent);
        });

        holder.tvStats.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(context,
                    com.example.ensatecertnotes.ui.prof.ModuleStatsActivity.class);
            intent.putExtra("MODULE_ID", module.getId());
            intent.putExtra("MODULE_NAME", module.getNomModule());
            context.startActivity(intent);
        });

        // Edit Module
        holder.itemView.setOnLongClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(context,
                    com.example.ensatecertnotes.ui.prof.AddModuleActivity.class);
            intent.putExtra("MODULE_ID", module.getId());
            context.startActivity(intent);
            return true;
        });

        // Or we can add an explicit "Modifier" button if we want to be more
        // user-friendly
        // For now, let's stick to the plan: edit and delete options.
        // I will add a click listener to the "Gérer >" text for standard management
        // (students)
        // and maybe an icon for delete.
    }

    @Override
    public int getItemCount() {
        return moduleList.size();
    }

    public static class ModuleViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCode, tvSemestre, tvStats;

        public ModuleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_module_name);
            tvCode = itemView.findViewById(R.id.tv_module_code);
            tvSemestre = itemView.findViewById(R.id.tv_module_semestre);
            tvStats = itemView.findViewById(R.id.btn_stats_module);
        }
    }
}
