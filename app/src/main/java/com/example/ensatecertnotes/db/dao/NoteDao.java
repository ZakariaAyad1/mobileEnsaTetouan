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
        values.put("etudiant_id", note.getEtudiantId());
        values.put("module_id", note.getModuleId()); // Ensure module_id is part of the update/insert
        if (note.getNoteExamen() != null)
            values.put("note_examen", note.getNoteExamen());
        if (note.getNoteTd() != null)
            values.put("note_td", note.getNoteTd());
        if (note.getNoteTp() != null)
            values.put("note_tp", note.getNoteTp());
        // note_finale and statut are calculated by TRIGGERS in DB, we don't necessarily
        // update them manually
        // unless we want to override trigger logic, but requirements say "Calcul
        // automatique".
        // However, if we read them back, we need them in model.
        if (note.getObservation() != null)
            values.put("observation", note.getObservation());

        // Check if exists
        Note existing = getNote(note.getEtudiantId(), note.getModuleId());
        if (existing != null) {
            db.update("notes", values, "id = ?", new String[] { String.valueOf(existing.getId()) });
        } else {
            db.insert("notes", null, values);
        }
    }
}
