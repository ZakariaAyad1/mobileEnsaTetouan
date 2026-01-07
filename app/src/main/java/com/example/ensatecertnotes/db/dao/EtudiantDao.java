package com.example.ensatecertnotes.db.dao;

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
}
