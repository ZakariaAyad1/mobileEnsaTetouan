
package com.example.ensatecertnotes.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.ensatecertnotes.db.DatabaseHelper;
import com.example.ensatecertnotes.model.Etudiant;

public class EtudiantDao_etudiant {
    private DatabaseHelper dbHelper;

    public EtudiantDao_etudiant(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * Get student by CNE
     */
    /**
     * Get student by CNE
     */
    public Etudiant getEtudiantByCNE(String cne) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Etudiant etudiant = null;

        String query = "SELECT * FROM etudiants WHERE cne = ?";
        Cursor cursor = db.rawQuery(query, new String[]{cne});

        if (cursor.moveToFirst()) {
            etudiant = mapCursorToEtudiant(cursor);
        }
        cursor.close();
        return etudiant;
    }

    /**
     * Get student by CNE or Email
     */
    public Etudiant getEtudiantByCneOrEmail(String identifier) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Etudiant etudiant = null;

        String query = "SELECT e.* FROM etudiants e " +
                       "JOIN users u ON e.user_id = u.id " +
                       "WHERE e.cne = ? OR u.email = ?";
        Cursor cursor = db.rawQuery(query, new String[]{identifier, identifier});

        if (cursor.moveToFirst()) {
            etudiant = mapCursorToEtudiant(cursor);
        }
        cursor.close();
        return etudiant;
    }

    // Helper method to map cursor to object to avoid code duplication
    private Etudiant mapCursorToEtudiant(Cursor cursor) {
        Etudiant etudiant = new Etudiant();
        etudiant.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        etudiant.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
        etudiant.setCne(cursor.getString(cursor.getColumnIndexOrThrow("cne")));
        etudiant.setNom(cursor.getString(cursor.getColumnIndexOrThrow("nom")));
        etudiant.setPrenom(cursor.getString(cursor.getColumnIndexOrThrow("prenom")));
        etudiant.setFiliere(cursor.getString(cursor.getColumnIndexOrThrow("filiere")));
        etudiant.setAnneeEtude(cursor.getString(cursor.getColumnIndexOrThrow("annee_etude")));
        etudiant.setPhotoUrl(cursor.getString(cursor.getColumnIndexOrThrow("photo_url")));
        return etudiant;
    }

    /**
     * Get student by user ID
     */
    public Etudiant getEtudiantByUserId(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Etudiant etudiant = null;

        String query = "SELECT * FROM etudiants WHERE user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            etudiant = new Etudiant();
            etudiant.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            etudiant.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
            etudiant.setCne(cursor.getString(cursor.getColumnIndexOrThrow("cne")));
            etudiant.setNom(cursor.getString(cursor.getColumnIndexOrThrow("nom")));
            etudiant.setPrenom(cursor.getString(cursor.getColumnIndexOrThrow("prenom")));
            etudiant.setFiliere(cursor.getString(cursor.getColumnIndexOrThrow("filiere")));
            etudiant.setAnneeEtude(cursor.getString(cursor.getColumnIndexOrThrow("annee_etude")));
            etudiant.setPhotoUrl(cursor.getString(cursor.getColumnIndexOrThrow("photo_url")));
        }
        cursor.close();
        return etudiant;
    }

    /**
     * Get student by ID
     */
    public Etudiant getEtudiantById(int etudiantId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Etudiant etudiant = null;

        String query = "SELECT * FROM etudiants WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(etudiantId)});

        if (cursor.moveToFirst()) {
            etudiant = new Etudiant();
            etudiant.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            etudiant.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
            etudiant.setCne(cursor.getString(cursor.getColumnIndexOrThrow("cne")));
            etudiant.setNom(cursor.getString(cursor.getColumnIndexOrThrow("nom")));
            etudiant.setPrenom(cursor.getString(cursor.getColumnIndexOrThrow("prenom")));
            etudiant.setFiliere(cursor.getString(cursor.getColumnIndexOrThrow("filiere")));
            etudiant.setAnneeEtude(cursor.getString(cursor.getColumnIndexOrThrow("annee_etude")));
            etudiant.setPhotoUrl(cursor.getString(cursor.getColumnIndexOrThrow("photo_url")));
        }
        cursor.close();
        return etudiant;
    }

    /**
     * Update student profile
     */
    public boolean updateProfile(Etudiant etudiant) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put("nom", etudiant.getNom());
        values.put("prenom", etudiant.getPrenom());
        values.put("photo_url", etudiant.getPhotoUrl());

        int rowsAffected = db.update(
            "etudiants",
            values,
            "id = ?",
            new String[]{String.valueOf(etudiant.getId())}
        );

        return rowsAffected > 0;
    }
}

