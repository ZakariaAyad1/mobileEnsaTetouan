/*salma*/
package com.example.ensatecertnotes.ui.admin.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.model.Notification;
import android.content.Intent; // salma
import com.example.ensatecertnotes.ui.admin.ListeDemandesActivity; // salma
import com.example.ensatecertnotes.ui.student.SuiviDemandesActivity_etudiant; // salma
import java.util.List;

public class NotificationAdapter_etudiant extends RecyclerView.Adapter<NotificationAdapter_etudiant.NotificationViewHolder> {

    private Context context;
    private List<Notification> notifications;
    private boolean isStudent; // salma

    public NotificationAdapter_etudiant(Context context, List<Notification> notifications, boolean isStudent) {
        this.context = context;
        this.notifications = notifications;
        this.isStudent = isStudent;
    }
    
    // maintain compatibility
    public NotificationAdapter_etudiant(Context context, List<Notification> notifications) {
         this(context, notifications, false);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification_etudiant, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);

        holder.tvTitre.setText(notification.getTitre());
        holder.tvMessage.setText(notification.getMessage());
        holder.tvDate.setText(notification.getDateCreation());

        // Indicateur lu/non lu
        if (notification.getLu() == 0) {
            holder.ivStatutLu.setVisibility(View.VISIBLE);
            holder.container.setBackgroundColor(Color.parseColor("#FFF8E1")); // Highlight background
        } else {
            holder.ivStatutLu.setVisibility(View.GONE);
            holder.container.setBackgroundColor(Color.WHITE);
        }

        // Icone selon le type
        switch (notification.getType()) {
            case "INFO":
                holder.ivIcon.setImageResource(android.R.drawable.ic_dialog_info);
                holder.ivIcon.setColorFilter(Color.BLUE);
                break;
            case "SUCCES":
                holder.ivIcon.setImageResource(android.R.drawable.star_on);
                holder.ivIcon.setColorFilter(Color.GREEN);
                break;
            case "ALERTE":
            case "URGENT":
                holder.ivIcon.setImageResource(android.R.drawable.ic_dialog_alert);
                holder.ivIcon.setColorFilter(Color.RED);
                break;
            default:
                holder.ivIcon.setImageResource(android.R.drawable.ic_dialog_info);
                holder.ivIcon.setColorFilter(Color.GRAY);
        }

        /*salma*/
        /*salma*/
        holder.itemView.setOnClickListener(v -> {
            Class<?> targetActivity = isStudent ? SuiviDemandesActivity_etudiant.class : ListeDemandesActivity.class;
            Intent intent = new Intent(context, targetActivity);
            context.startActivity(intent);
        });
        /*salma*/
        /*salma*/
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitre, tvMessage, tvDate;
        ImageView ivIcon, ivStatutLu;
        LinearLayout container;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitre = itemView.findViewById(R.id.tv_titre_notif);
            tvMessage = itemView.findViewById(R.id.tv_message_notif);
            tvDate = itemView.findViewById(R.id.tv_date_notif);
            ivIcon = itemView.findViewById(R.id.iv_icon_type);
            ivStatutLu = itemView.findViewById(R.id.iv_statut_lu);
            container = itemView.findViewById(R.id.container_notif);
        }
    }
}
/*salma*/
