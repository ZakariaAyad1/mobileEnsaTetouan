
package com.example.ensatecertnotes.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.ensatecertnotes.db.DatabaseHelper;
import com.example.ensatecertnotes.model.Note;
import com.example.ensatecertnotes.model.Module;
import java.util.ArrayList;
import java.util.List;

public class NoteDao_etudiant {
    private DatabaseHelper dbHelper;

    public NoteDao_etudiant(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    /**
     * Get grades by semester for a student
     */
    public List<NoteWithModule> getNotesBySemestre(int etudiantId, int semestre) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<NoteWithModule> notes = new ArrayList<>();

        String query = "SELECT n.*, m.code_module as module_code, m.nom_module as module_nom, " +
                      "m.coefficient as credits, m.semestre " +
                      "FROM notes n " +
                      "INNER JOIN modules m ON n.module_id = m.id " +
                      "WHERE n.etudiant_id = ? AND m.semestre = ? " +
                      "ORDER BY m.code_module";

        Cursor cursor = db.rawQuery(query, new String[]{
            String.valueOf(etudiantId),
            String.valueOf(semestre)
        });

        while (cursor.moveToNext()) {
            Note note = new Note();
            note.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            note.setEtudiantId(cursor.getInt(cursor.getColumnIndexOrThrow("etudiant_id")));
            note.setModuleId(cursor.getInt(cursor.getColumnIndexOrThrow("module_id")));
            
            // Handle NULL values for grades
            int examenIndex = cursor.getColumnIndexOrThrow("note_examen");
            note.setNoteExamen(cursor.isNull(examenIndex) ? null : cursor.getDouble(examenIndex));
            
            int tdIndex = cursor.getColumnIndexOrThrow("note_td");
            note.setNoteTd(cursor.isNull(tdIndex) ? null : cursor.getDouble(tdIndex));
            
            int tpIndex = cursor.getColumnIndexOrThrow("note_tp");
            note.setNoteTp(cursor.isNull(tpIndex) ? null : cursor.getDouble(tpIndex));
            
            int finaleIndex = cursor.getColumnIndexOrThrow("note_finale");
            note.setNoteFinale(cursor.isNull(finaleIndex) ? null : cursor.getDouble(finaleIndex));
            
            note.setStatut(cursor.getString(cursor.getColumnIndexOrThrow("statut")));
            note.setObservation(cursor.getString(cursor.getColumnIndexOrThrow("observation")));

            // Module info
            String moduleCode = cursor.getString(cursor.getColumnIndexOrThrow("module_code"));
            String moduleNom = cursor.getString(cursor.getColumnIndexOrThrow("module_nom"));
            int credits = cursor.getInt(cursor.getColumnIndexOrThrow("credits"));

            notes.add(new NoteWithModule(note, moduleCode, moduleNom, credits));
        }
        cursor.close();
        return notes;
    }

    /**
     * Get detailed grades for a specific module
     */
    public NoteWithModule getNotesDetailsByModule(int etudiantId, int moduleId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        NoteWithModule result = null;

        String query = "SELECT n.*, m.code_module as module_code, m.nom_module as module_nom, " +
                      "m.coefficient as credits, m.semestre " +
                      "FROM notes n " +
                      "INNER JOIN modules m ON n.module_id = m.id " +
                      "WHERE n.etudiant_id = ? AND n.module_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{
            String.valueOf(etudiantId),
            String.valueOf(moduleId)
        });

        if (cursor.moveToFirst()) {
            Note note = new Note();
            note.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            note.setEtudiantId(cursor.getInt(cursor.getColumnIndexOrThrow("etudiant_id")));
            note.setModuleId(cursor.getInt(cursor.getColumnIndexOrThrow("module_id")));
            
            int examenIndex = cursor.getColumnIndexOrThrow("note_examen");
            note.setNoteExamen(cursor.isNull(examenIndex) ? null : cursor.getDouble(examenIndex));
            
            int tdIndex = cursor.getColumnIndexOrThrow("note_td");
            note.setNoteTd(cursor.isNull(tdIndex) ? null : cursor.getDouble(tdIndex));
            
            int tpIndex = cursor.getColumnIndexOrThrow("note_tp");
            note.setNoteTp(cursor.isNull(tpIndex) ? null : cursor.getDouble(tpIndex));
            
            int finaleIndex = cursor.getColumnIndexOrThrow("note_finale");
            note.setNoteFinale(cursor.isNull(finaleIndex) ? null : cursor.getDouble(finaleIndex));
            
            note.setStatut(cursor.getString(cursor.getColumnIndexOrThrow("statut")));
            note.setObservation(cursor.getString(cursor.getColumnIndexOrThrow("observation")));

            String moduleCode = cursor.getString(cursor.getColumnIndexOrThrow("module_code"));
            String moduleNom = cursor.getString(cursor.getColumnIndexOrThrow("module_nom"));
            int credits = cursor.getInt(cursor.getColumnIndexOrThrow("credits"));

            result = new NoteWithModule(note, moduleCode, moduleNom, credits);
        }
        cursor.close();
        return result;
    }

    /**
     * Get all grades for a student
     */
    public List<NoteWithModule> getAllNotesByEtudiant(int etudiantId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<NoteWithModule> notes = new ArrayList<>();

        String query = "SELECT n.*, m.code_module as module_code, m.nom_module as module_nom, " +
                      "m.coefficient as credits, m.semestre " +
                      "FROM notes n " +
                      "INNER JOIN modules m ON n.module_id = m.id " +
                      "WHERE n.etudiant_id = ? " +
                      "ORDER BY m.semestre, m.code_module";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(etudiantId)});

        while (cursor.moveToNext()) {
            Note note = new Note();
            note.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            note.setEtudiantId(cursor.getInt(cursor.getColumnIndexOrThrow("etudiant_id")));
            note.setModuleId(cursor.getInt(cursor.getColumnIndexOrThrow("module_id")));
            
            int examenIndex = cursor.getColumnIndexOrThrow("note_examen");
            note.setNoteExamen(cursor.isNull(examenIndex) ? null : cursor.getDouble(examenIndex));
            
            int tdIndex = cursor.getColumnIndexOrThrow("note_td");
            note.setNoteTd(cursor.isNull(tdIndex) ? null : cursor.getDouble(tdIndex));
            
            int tpIndex = cursor.getColumnIndexOrThrow("note_tp");
            note.setNoteTp(cursor.isNull(tpIndex) ? null : cursor.getDouble(tpIndex));
            
            int finaleIndex = cursor.getColumnIndexOrThrow("note_finale");
            note.setNoteFinale(cursor.isNull(finaleIndex) ? null : cursor.getDouble(finaleIndex));
            
            note.setStatut(cursor.getString(cursor.getColumnIndexOrThrow("statut")));
            note.setObservation(cursor.getString(cursor.getColumnIndexOrThrow("observation")));

            String moduleCode = cursor.getString(cursor.getColumnIndexOrThrow("module_code"));
            String moduleNom = cursor.getString(cursor.getColumnIndexOrThrow("module_nom"));
            int credits = cursor.getInt(cursor.getColumnIndexOrThrow("credits"));

            notes.add(new NoteWithModule(note, moduleCode, moduleNom, credits));
        }
        cursor.close();
        return notes;
    }

    /**
     * Helper class to combine Note with Module information
     */
    public static class NoteWithModule {
        private Note note;
        private String moduleCode;
        private String moduleNom;
        private int credits;

        public NoteWithModule(Note note, String moduleCode, String moduleNom, int credits) {
            this.note = note;
            this.moduleCode = moduleCode;
            this.moduleNom = moduleNom;
            this.credits = credits;
        }

        public Note getNote() {
            return note;
        }

        public String getModuleCode() {
            return moduleCode;
        }

        public String getModuleNom() {
            return moduleNom;
        }

        public int getCredits() {
            return credits;
        }
    }
}

