
package com.example.ensatecertnotes.ui.admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.dao.NotificationDao_etudiant;
import com.example.ensatecertnotes.model.Notification;
import com.example.ensatecertnotes.ui.admin.adapters.NotificationAdapter_etudiant;
import java.util.List;

public class NotificationActivity_etudiant extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvEmpty;
    private NotificationDao_etudiant notificationDao;
    private SharedPreferences sharedPreferences;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_etudiant);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Notifications");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recycler_notifications);
        tvEmpty = findViewById(R.id.tv_empty_notif);
        notificationDao = new NotificationDao_etudiant(this);
        sharedPreferences = getSharedPreferences("AdminSession", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        loadNotifications();
        
        // Mark all as read when opening the activity
        if (userId != -1) {
            notificationDao.markAllAsRead(userId);
        }
    }

    private void loadNotifications() {
        if (userId == -1) return;

        List<Notification> notificationList = notificationDao.getNotifications(userId);

        if (notificationList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            
            NotificationAdapter_etudiant adapter = new NotificationAdapter_etudiant(this, notificationList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

