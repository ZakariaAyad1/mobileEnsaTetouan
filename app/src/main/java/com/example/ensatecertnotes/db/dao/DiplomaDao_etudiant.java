
package com.example.ensatecertnotes.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.ensatecertnotes.db.DatabaseHelper;
import com.example.ensatecertnotes.model.DemandeDiplome_etudiant;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DiplomaDao_etudiant {
    private DatabaseHelper dbHelper;
    private static final String TABLE_NAME = "demandes_diplomes";

    public DiplomaDao_etudiant(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * Create a new diploma request
     */
    public long createDemandeDiplome(DemandeDiplome_etudiant diplome) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put("etudiant_id", diplome.getEtudiantId());
        values.put("date_demande", getCurrentDate());
        values.put("statut", "EN_ATTENTE");
        values.put("adresse_livraison", diplome.getAdresseLivraison());
        values.put("telephone", diplome.getTelephone());
        values.put("commentaire", diplome.getCommentaire());

        return db.insert(TABLE_NAME, null, values);
    }

    /**
     * Get diploma request for a student
     */
    public DemandeDiplome_etudiant getDemandeDiplomeByEtudiant(int etudiantId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        DemandeDiplome_etudiant diplome = null;

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE etudiant_id = ? ORDER BY date_demande DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(etudiantId)});

        if (cursor.moveToFirst()) {
            diplome = new DemandeDiplome_etudiant();
            diplome.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            diplome.setEtudiantId(cursor.getInt(cursor.getColumnIndexOrThrow("etudiant_id")));
            diplome.setDateDemande(cursor.getString(cursor.getColumnIndexOrThrow("date_demande")));
            diplome.setStatut(cursor.getString(cursor.getColumnIndexOrThrow("statut")));
            
            int dateTraitementIndex = cursor.getColumnIndexOrThrow("date_traitement");
            diplome.setDateTraitement(cursor.isNull(dateTraitementIndex) ? null : 
                cursor.getString(dateTraitementIndex));
            
            int adresseIndex = cursor.getColumnIndexOrThrow("adresse_livraison");
            diplome.setAdresseLivraison(cursor.isNull(adresseIndex) ? null : 
                cursor.getString(adresseIndex));
            
            int telephoneIndex = cursor.getColumnIndexOrThrow("telephone");
            diplome.setTelephone(cursor.isNull(telephoneIndex) ? null : 
                cursor.getString(telephoneIndex));
            
            int commentaireIndex = cursor.getColumnIndexOrThrow("commentaire");
            diplome.setCommentaire(cursor.isNull(commentaireIndex) ? null : 
                cursor.getString(commentaireIndex));
        }
        cursor.close();
        return diplome;
    }

    /**
     * Update diploma request
     */
    public boolean updateDemandeDiplome(DemandeDiplome_etudiant diplome) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        ContentValues values = new ContentValues();
        values.put("adresse_livraison", diplome.getAdresseLivraison());
        values.put("telephone", diplome.getTelephone());
        values.put("commentaire", diplome.getCommentaire());

        int rowsAffected = db.update(
            TABLE_NAME,
            values,
            "id = ?",
            new String[]{String.valueOf(diplome.getId())}
        );

        return rowsAffected > 0;
    }

    /**
     * Create table if it doesn't exist (for compatibility)
     */
    private void createTableIfNotExists(SQLiteDatabase db) {
        String createTable = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "etudiant_id INTEGER NOT NULL, " +
            "date_demande TEXT NOT NULL, " +
            "statut TEXT NOT NULL DEFAULT 'EN_ATTENTE', " +
            "date_traitement TEXT, " +
            "adresse_livraison TEXT, " +
            "telephone TEXT, " +
            "commentaire TEXT, " +
            "FOREIGN KEY (etudiant_id) REFERENCES etudiants(id) ON DELETE CASCADE" +
            ")";
        db.execSQL(createTable);
    }

    /**
     * Get current date in SQL format
     */
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}

