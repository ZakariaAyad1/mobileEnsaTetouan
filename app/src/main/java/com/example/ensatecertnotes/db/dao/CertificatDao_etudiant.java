/*salma*/
package com.example.ensatecertnotes.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.ensatecertnotes.db.DatabaseHelper;
import com.example.ensatecertnotes.model.Certificat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CertificatDao_etudiant {
    private DatabaseHelper dbHelper;

    public CertificatDao_etudiant(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * Create a new certificate request
     */
    public long createDemandeCertificat(Certificat certificat) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("etudiant_id", certificat.getEtudiantId());
        values.put("type_certificat", certificat.getType());
        values.put("date_demande", getCurrentDate());
        values.put("statut", "EN_ATTENTE");
        values.put("motif", certificat.getMotif());

        return db.insert("demandes_certificats", null, values);
    }

    /**
     * Get all certificate requests for a student
     */
    public List<Certificat> getDemandesByEtudiant(int etudiantId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Certificat> demandes = new ArrayList<>();

        String query = "SELECT * FROM demandes_certificats WHERE etudiant_id = ? ORDER BY date_demande DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(etudiantId)});

        while (cursor.moveToNext()) {
            Certificat certificat = new Certificat();
            certificat.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            certificat.setEtudiantId(cursor.getInt(cursor.getColumnIndexOrThrow("etudiant_id")));
            certificat.setType(cursor.getString(cursor.getColumnIndexOrThrow("type_certificat")));
            certificat.setDateDemande(cursor.getString(cursor.getColumnIndexOrThrow("date_demande")));
            certificat.setStatut(cursor.getString(cursor.getColumnIndexOrThrow("statut")));
            
            int dateTraitementIndex = cursor.getColumnIndexOrThrow("date_traitement");
            certificat.setDateTraitement(cursor.isNull(dateTraitementIndex) ? null : 
                cursor.getString(dateTraitementIndex));
            
            certificat.setMotif(cursor.getString(cursor.getColumnIndexOrThrow("motif")));
            
            int commentaireIndex = cursor.getColumnIndexOrThrow("commentaire");
            certificat.setCommentaire(cursor.isNull(commentaireIndex) ? null : 
                cursor.getString(commentaireIndex));

            demandes.add(certificat);
        }
        cursor.close();
        return demandes;
    }

    /**
     * Get a specific certificate request by ID
     */
    public Certificat getDemandeById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Certificat certificat = null;

        String query = "SELECT * FROM demandes_certificats WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            certificat = new Certificat();
            certificat.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            certificat.setEtudiantId(cursor.getInt(cursor.getColumnIndexOrThrow("etudiant_id")));
            certificat.setType(cursor.getString(cursor.getColumnIndexOrThrow("type_certificat")));
            certificat.setDateDemande(cursor.getString(cursor.getColumnIndexOrThrow("date_demande")));
            certificat.setStatut(cursor.getString(cursor.getColumnIndexOrThrow("statut")));
            
            int dateTraitementIndex = cursor.getColumnIndexOrThrow("date_traitement");
            certificat.setDateTraitement(cursor.isNull(dateTraitementIndex) ? null : 
                cursor.getString(dateTraitementIndex));
            
            certificat.setMotif(cursor.getString(cursor.getColumnIndexOrThrow("motif")));
            
            int commentaireIndex = cursor.getColumnIndexOrThrow("commentaire");
            certificat.setCommentaire(cursor.isNull(commentaireIndex) ? null : 
                cursor.getString(commentaireIndex));
        }
        cursor.close();
        return certificat;
    }

    /**
     * Get current date in SQL format
     */
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
/*salma*/
