package com.example.ensatecertnotes.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ensatecertnotes.db.DatabaseHelper;

public class InscriptionDao {
    private DatabaseHelper dbHelper;

    public InscriptionDao(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    public long enrollStudent(int etudiantId, int moduleId, String anneeUniversitaire) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("etudiant_id", etudiantId);
        values.put("module_id", moduleId);
        values.put("annee_universitaire", anneeUniversitaire);
        return db.insert("inscriptions", null, values);
    }

    public int unenrollStudent(int etudiantId, int moduleId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("inscriptions",
                "etudiant_id = ? AND module_id = ?",
                new String[] { String.valueOf(etudiantId), String.valueOf(moduleId) });
    }

    public boolean isStudentEnrolled(int etudiantId, int moduleId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("inscriptions",
                null,
                "etudiant_id = ? AND module_id = ?",
                new String[] { String.valueOf(etudiantId), String.valueOf(moduleId) },
                null, null, null);
        boolean enrolled = cursor != null && cursor.getCount() > 0;
        if (cursor != null)
            cursor.close();
        return enrolled;
    }
}
