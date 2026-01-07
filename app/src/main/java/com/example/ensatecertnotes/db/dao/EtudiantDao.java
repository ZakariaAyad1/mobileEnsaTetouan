package com.example.ensatecertnotes.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ensatecertnotes.db.DatabaseHelper;
import com.example.ensatecertnotes.model.Etudiant;

import java.util.ArrayList;
import java.util.List;

public class EtudiantDao {
    private DatabaseHelper dbHelper;

    public EtudiantDao(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    public List<Etudiant> getEtudiantsByModule(int moduleId) {
        List<Etudiant> etudiants = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Jointure pour récupérer les étudiants inscrits à ce module
        String sql = "SELECT e.* FROM etudiants e " +
                "JOIN inscriptions i ON e.id = i.etudiant_id " +
                "WHERE i.module_id = ?";

        Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(moduleId) });

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Etudiant etudiant = new Etudiant(
                            cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                            cursor.getString(cursor.getColumnIndexOrThrow("cne")),
                            cursor.getString(cursor.getColumnIndexOrThrow("nom")),
                            cursor.getString(cursor.getColumnIndexOrThrow("prenom")),
                            cursor.getString(cursor.getColumnIndexOrThrow("filiere")),
                            cursor.getString(cursor.getColumnIndexOrThrow("annee_etude")),
                            cursor.getString(cursor.getColumnIndexOrThrow("photo_url")));
                    etudiants.add(etudiant);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return etudiants;
    }

    public long addStudent(Etudiant etudiant) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", etudiant.getUserId());
        values.put("cne", etudiant.getCne());
        values.put("nom", etudiant.getNom());
        values.put("prenom", etudiant.getPrenom());
        values.put("filiere", etudiant.getFiliere());
        values.put("annee_etude", etudiant.getAnneeEtude());
        return db.insert("etudiants", null, values);
    }

    public int updateStudent(Etudiant etudiant) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cne", etudiant.getCne());
        values.put("nom", etudiant.getNom());
        values.put("prenom", etudiant.getPrenom());
        values.put("filiere", etudiant.getFiliere());
        values.put("annee_etude", etudiant.getAnneeEtude());
        return db.update("etudiants", values, "id = ?", new String[] { String.valueOf(etudiant.getId()) });
    }

    public int deleteStudent(int etudiantId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("etudiants", "id = ?", new String[] { String.valueOf(etudiantId) });
    }

    public List<Etudiant> getAllEtudiants() {
        List<Etudiant> etudiants = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM etudiants", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    etudiants.add(new Etudiant(
                            cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                            cursor.getString(cursor.getColumnIndexOrThrow("cne")),
                            cursor.getString(cursor.getColumnIndexOrThrow("nom")),
                            cursor.getString(cursor.getColumnIndexOrThrow("prenom")),
                            cursor.getString(cursor.getColumnIndexOrThrow("filiere")),
                            cursor.getString(cursor.getColumnIndexOrThrow("annee_etude")),
                            cursor.getString(cursor.getColumnIndexOrThrow("photo_url"))));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return etudiants;
    }

    public Etudiant getEtudiantById(int etudiantId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM etudiants WHERE id = ?",
                new String[] { String.valueOf(etudiantId) });
        if (cursor != null && cursor.moveToFirst()) {
            Etudiant etudiant = new Etudiant(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("user_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("cne")),
                    cursor.getString(cursor.getColumnIndexOrThrow("nom")),
                    cursor.getString(cursor.getColumnIndexOrThrow("prenom")),
                    cursor.getString(cursor.getColumnIndexOrThrow("filiere")),
                    cursor.getString(cursor.getColumnIndexOrThrow("annee_etude")),
                    cursor.getString(cursor.getColumnIndexOrThrow("photo_url")));
            cursor.close();
            return etudiant;
        }
        return null;
    }
}
