package com.example.ensatecertnotes.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ensatecertnotes.db.DatabaseHelper;
import com.example.ensatecertnotes.model.Note;

public class NoteDao {
    private DatabaseHelper dbHelper;

    public NoteDao(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    public Note getNote(int etudiantId, int moduleId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM notes WHERE etudiant_id = ? AND module_id = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(etudiantId), String.valueOf(moduleId) });

        if (cursor != null && cursor.moveToFirst()) {
            Note note = new Note(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("etudiant_id")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("module_id")),
                    cursor.isNull(cursor.getColumnIndexOrThrow("note_examen")) ? null
                            : cursor.getDouble(cursor.getColumnIndexOrThrow("note_examen")),
                    cursor.isNull(cursor.getColumnIndexOrThrow("note_td")) ? null
                            : cursor.getDouble(cursor.getColumnIndexOrThrow("note_td")),
                    cursor.isNull(cursor.getColumnIndexOrThrow("note_tp")) ? null
                            : cursor.getDouble(cursor.getColumnIndexOrThrow("note_tp")),
                    cursor.isNull(cursor.getColumnIndexOrThrow("note_finale")) ? null
                            : cursor.getDouble(cursor.getColumnIndexOrThrow("note_finale")),
                    cursor.getString(cursor.getColumnIndexOrThrow("statut")),
                    cursor.getString(cursor.getColumnIndexOrThrow("observation")));
            cursor.close();
            return note;
        }
        return null;
    }

    public void saveOrUpdateNote(Note note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Calcul manuel pour assurer la cohérence immédiate même sans triggers
        // récursifs
        double nEx = note.getNoteExamen() != null ? note.getNoteExamen() : 0;
        double nTd = note.getNoteTd() != null ? note.getNoteTd() : 0;
        double nTp = note.getNoteTp() != null ? note.getNoteTp() : 0;
        double finale = (nEx * 0.4) + (nTd * 0.3) + (nTp * 0.3);

        String statut;
        if (finale >= 10)
            statut = "VALIDE";
        else if (finale >= 7)
            statut = "RATTRAPAGE";
        else
            statut = "NON_VALIDE";

        values.put("etudiant_id", note.getEtudiantId());
        values.put("module_id", note.getModuleId());
        values.put("note_examen", note.getNoteExamen());
        values.put("note_td", note.getNoteTd());
        values.put("note_tp", note.getNoteTp());
        values.put("note_finale", finale);
        values.put("statut", statut);
        values.put("observation", note.getObservation());

        // Check if exists
        Note existing = getNote(note.getEtudiantId(), note.getModuleId());
        if (existing != null) {
            db.update("notes", values, "id = ?", new String[] { String.valueOf(existing.getId()) });
        } else {
            db.insert("notes", null, values);
        }
    }

    public double getValidationRateByProfesseur(int professeurId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Calculate percentage of validated notes (statut = 'VALIDE') for modules of
        // this prof
        String sql = "SELECT " +
                "COUNT(CASE WHEN n.statut = 'VALIDE' THEN 1 END) as valides, " +
                "COUNT(*) as total " +
                "FROM notes n " +
                "JOIN modules m ON n.module_id = m.id " +
                "WHERE m.professeur_id = ? AND n.note_finale IS NOT NULL";

        Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(professeurId) });
        double rate = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int valides = cursor.getInt(0);
                int total = cursor.getInt(1);
                if (total > 0) {
                    rate = (double) valides / total * 100;
                }
            }
            cursor.close();
        }
        return rate;
    }

    // --- Module Specific Stats ---

    public double getAverageGrade(int moduleId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT AVG(note_finale) FROM notes WHERE module_id = ? AND note_finale IS NOT NULL";
        Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(moduleId) });
        double avg = 0;
        if (cursor != null) {
            if (cursor.moveToFirst())
                avg = cursor.getDouble(0);
            cursor.close();
        }
        return avg;
    }

    public double getMinGrade(int moduleId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT MIN(note_finale) FROM notes WHERE module_id = ? AND note_finale IS NOT NULL";
        Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(moduleId) });
        double min = 0;
        if (cursor != null) {
            if (cursor.moveToFirst())
                min = cursor.getDouble(0);
            cursor.close();
        }
        return min;
    }

    public double getMaxGrade(int moduleId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT MAX(note_finale) FROM notes WHERE module_id = ? AND note_finale IS NOT NULL";
        Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(moduleId) });
        double max = 0;
        if (cursor != null) {
            if (cursor.moveToFirst())
                max = cursor.getDouble(0);
            cursor.close();
        }
        return max;
    }

    public int getCountByStatus(int moduleId, String status) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM notes WHERE module_id = ? AND statut = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(moduleId), status });
        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst())
                count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }
}
