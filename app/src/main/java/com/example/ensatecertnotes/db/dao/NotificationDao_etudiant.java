package com.example.ensatecertnotes.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.ensatecertnotes.db.DatabaseHelper;
import com.example.ensatecertnotes.model.Notification;
import java.util.ArrayList;
import java.util.List;

/*salma*/
public class NotificationDao_etudiant {

    private DatabaseHelper dbHelper;

    public NotificationDao_etudiant(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    // Méthode pour notifier TOUS les admins (Agents et Managers)
    public void notifyAdmins(String titre, String message, String type) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        // 1. Récupérer les IDs des admins (users avec role AGENT ou MANAGER)
        // Mais attention, la table 'users' a le role. "AGENT" et "MANAGER" sont les valeurs dans la table users ?
        // Vérifions db.sql: role CHECK(role IN ('PROFESSEUR', 'ETUDIANT', 'AGENT', 'MANAGER'))
        // Donc on cherche dans users where role IN ('AGENT', 'MANAGER')
        
        List<Integer> adminUserIds = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE role IN ('AGENT', 'MANAGER')", null);
        if (cursor.moveToFirst()) {
            do {
                adminUserIds.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();

        // 2. Insérer une notification pour chaque admin
        for (Integer userId : adminUserIds) {
            ContentValues values = new ContentValues();
            values.put("user_id", userId);
            values.put("titre", titre);
            values.put("message", message);
            values.put("type", type);
            values.put("lu", 0);
            // date_creation est DEFAULT CURRENT_TIMESTAMP
            
            db.insert("notifications", null, values);
        }
        
        // Pas de fermeture de db ici si on utilise le Singleton de manière partagée, 
        // mais bonne pratique de ne pas fermer si d'autres l'utilisent. 
        // DatabaseHelper se charge souvent de la connexion.
    }

    // Compter les notifications non lues pour un user
    public int getUnreadCount(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int count = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM notifications WHERE user_id = ? AND lu = 0", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    /*salma*/
    public List<Notification> getNotifications(int userId) {
        List<Notification> notifications = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String query = "SELECT * FROM notifications WHERE user_id = ? ORDER BY date_creation DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        
        if (cursor.moveToFirst()) {
            do {
                Notification notif = new Notification();
                notif.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                notif.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                notif.setTitre(cursor.getString(cursor.getColumnIndexOrThrow("titre")));
                notif.setMessage(cursor.getString(cursor.getColumnIndexOrThrow("message")));
                notif.setType(cursor.getString(cursor.getColumnIndexOrThrow("type")));
                notif.setLu(cursor.getInt(cursor.getColumnIndexOrThrow("lu")));
                notif.setDateCreation(cursor.getString(cursor.getColumnIndexOrThrow("date_creation")));
                
                notifications.add(notif);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return notifications;
    }

    public void markAllAsRead(int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("lu", 1);
        db.update("notifications", values, "user_id = ? AND lu = 0", new String[]{String.valueOf(userId)});
    }
    
    public void deleteNotification(int notifId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("notifications", "id = ?", new String[]{String.valueOf(notifId)});
    }

    /*salma*/
    public void notifyStudent(int etudiantId, String titre, String message, String type) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        // Trouver le user_id associé à l'etudiant_id
        int userId = -1;
        Cursor cursor = db.rawQuery("SELECT user_id FROM etudiants WHERE id = ?", new String[]{String.valueOf(etudiantId)});
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();

        if (userId != -1) {
            ContentValues values = new ContentValues();
            values.put("user_id", userId);
            values.put("titre", titre);
            values.put("message", message);
            values.put("type", type);
            values.put("lu", 0);
            db.insert("notifications", null, values);
        }
    }
    /*salma*/
}
/*salma*/
