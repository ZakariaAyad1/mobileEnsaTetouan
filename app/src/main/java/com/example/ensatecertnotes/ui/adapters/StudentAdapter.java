package com.example.ensatecertnotes.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.model.Etudiant;
import com.example.ensatecertnotes.ui.prof.AddStudentActivity;
import com.example.ensatecertnotes.ui.prof.StudentDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private Context context;
    private List<Etudiant> studentList;
    private List<Etudiant> studentListFull; // For filtering

    public StudentAdapter(Context context, List<Etudiant> studentList) {
        this.context = context;
        this.studentList = studentList;
        this.studentListFull = new ArrayList<>(studentList);
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Etudiant student = studentList.get(position);
        holder.tvName.setText(student.getNom() + " " + student.getPrenom());
        holder.tvCne.setText("CNE: " + student.getCne());
        holder.tvFiliere.setText(student.getFiliere());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, StudentDetailActivity.class);
            intent.putExtra("STUDENT_ID", student.getId());
            context.startActivity(intent);
        });

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddStudentActivity.class);
            intent.putExtra("STUDENT_ID", student.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void updateList(List<Etudiant> newList) {
        studentList = newList;
        notifyDataSetChanged();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCne, tvFiliere;
        ImageView ivAvatar, btnEdit;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_student_full_name);
            tvCne = itemView.findViewById(R.id.tv_student_cne);
            tvFiliere = itemView.findViewById(R.id.tv_student_filiere);
            ivAvatar = itemView.findViewById(R.id.iv_student_avatar);
            btnEdit = itemView.findViewById(R.id.btn_edit_student);
        }
    }
}
