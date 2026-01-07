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
    }

    @Override
    public int getItemCount() {
        return moduleList.size();
    }

    public static class ModuleViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCode, tvSemestre;

        public ModuleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_module_name);
            tvCode = itemView.findViewById(R.id.tv_module_code);
            tvSemestre = itemView.findViewById(R.id.tv_module_semestre);
        }
    }
}
