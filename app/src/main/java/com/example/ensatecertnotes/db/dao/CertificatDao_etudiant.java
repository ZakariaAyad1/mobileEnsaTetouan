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

        // 1. Fetch Certificates
        String queryCert = "SELECT * FROM demandes_certificats WHERE etudiant_id = ?";
        Cursor cursorCert = db.rawQuery(queryCert, new String[]{String.valueOf(etudiantId)});

        while (cursorCert.moveToNext()) {
            Certificat certificat = new Certificat();
            certificat.setId(cursorCert.getInt(cursorCert.getColumnIndexOrThrow("id")));
            certificat.setEtudiantId(cursorCert.getInt(cursorCert.getColumnIndexOrThrow("etudiant_id")));
            certificat.setType(cursorCert.getString(cursorCert.getColumnIndexOrThrow("type_certificat")));
            certificat.setDateDemande(cursorCert.getString(cursorCert.getColumnIndexOrThrow("date_demande")));
            certificat.setStatut(cursorCert.getString(cursorCert.getColumnIndexOrThrow("statut")));
            
            int dateTraitementIndex = cursorCert.getColumnIndexOrThrow("date_traitement");
            certificat.setDateTraitement(cursorCert.isNull(dateTraitementIndex) ? null : 
                cursorCert.getString(dateTraitementIndex));
            
            certificat.setMotif(cursorCert.getString(cursorCert.getColumnIndexOrThrow("motif")));
            
            int commentaireIndex = cursorCert.getColumnIndexOrThrow("commentaire");
            certificat.setCommentaire(cursorCert.isNull(commentaireIndex) ? null : 
                cursorCert.getString(commentaireIndex));

            demandes.add(certificat);
        }
        cursorCert.close();

        // 2. Fetch Diplomas and map to Certificat
        String queryDip = "SELECT * FROM demandes_diplomes WHERE etudiant_id = ?";
        Cursor cursorDip = db.rawQuery(queryDip, new String[]{String.valueOf(etudiantId)});

        while (cursorDip.moveToNext()) {
            Certificat diplomeAsCert = new Certificat();
            diplomeAsCert.setId(cursorDip.getInt(cursorDip.getColumnIndexOrThrow("id")));
            diplomeAsCert.setEtudiantId(cursorDip.getInt(cursorDip.getColumnIndexOrThrow("etudiant_id")));
            diplomeAsCert.setType("DIPLOME"); // Special type for diplomas
            diplomeAsCert.setDateDemande(cursorDip.getString(cursorDip.getColumnIndexOrThrow("date_demande")));
            diplomeAsCert.setStatut(cursorDip.getString(cursorDip.getColumnIndexOrThrow("statut")));
            
            int dateTraitementIndex = cursorDip.getColumnIndexOrThrow("date_traitement");
            diplomeAsCert.setDateTraitement(cursorDip.isNull(dateTraitementIndex) ? null : 
                cursorDip.getString(dateTraitementIndex));
            
            // Map specific fields to motif/commentaire if needed, or leave blank
            // For now, we mainly need basic info for the list
            
            demandes.add(diplomeAsCert);
        }
        cursorDip.close();

        // 3. Sort by date desc
        java.util.Collections.sort(demandes, (d1, d2) -> {
            String date1 = d1.getDateDemande();
            String date2 = d2.getDateDemande();
            if (date1 == null) return 1;
            if (date2 == null) return -1;
            return date2.compareTo(date1);
        });

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
