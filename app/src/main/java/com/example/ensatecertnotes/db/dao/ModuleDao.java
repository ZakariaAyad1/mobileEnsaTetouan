package com.example.ensatecertnotes.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ensatecertnotes.db.DatabaseHelper;
import com.example.ensatecertnotes.model.Module;

import java.util.ArrayList;
import java.util.List;

public class ModuleDao {
    private DatabaseHelper dbHelper;

    public ModuleDao(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    public List<Module> getModulesByProfesseur(int professeurId) {
        List<Module> modules = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM modules WHERE professeur_id = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(professeurId) });

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Module module = new Module(
                            cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                            cursor.getString(cursor.getColumnIndexOrThrow("code_module")),
                            cursor.getString(cursor.getColumnIndexOrThrow("nom_module")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("semestre")),
                            cursor.getDouble(cursor.getColumnIndexOrThrow("coefficient")),
                            cursor.getInt(cursor.getColumnIndexOrThrow("professeur_id")),
                            cursor.getString(cursor.getColumnIndexOrThrow("annee_universitaire")));
                    modules.add(module);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return modules;
    }

    public int countStudentsByProfesseur(int professeurId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Count distinct students enrolled in modules taught by this professor
        String sql = "SELECT COUNT(DISTINCT i.etudiant_id) " +
                "FROM inscriptions i " +
                "JOIN modules m ON i.module_id = m.id " +
                "WHERE m.professeur_id = ?";
        Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(professeurId) });
        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }
}
