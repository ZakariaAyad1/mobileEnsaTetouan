package com.example.ensatecertnotes.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ensatecertnotes.db.DatabaseHelper;
import com.example.ensatecertnotes.model.Professeur;

public class ProfesseurDao {
    private DatabaseHelper dbHelper;

    public ProfesseurDao(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    public Professeur getProfesseurByUserId(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM professeurs WHERE user_id = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(userId) });

        if (cursor != null && cursor.moveToFirst()) {
            Professeur professeur = new Professeur(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("nom")),
                    cursor.getString(cursor.getColumnIndexOrThrow("prenom")),
                    cursor.getString(cursor.getColumnIndexOrThrow("departement")),
                    cursor.getString(cursor.getColumnIndexOrThrow("telephone")),
                    cursor.getString(cursor.getColumnIndexOrThrow("photo_url")));
            cursor.close();
            return professeur;
        }
        return null;
    }
}
