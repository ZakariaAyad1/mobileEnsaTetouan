package com.example.ensatecertnotes.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "certifnotes.db";
    // Si vous modifiez db.sql, incrémentez cette version pour forcer la mise à jour
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "DatabaseHelper";
    private final Context context;

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        // Active les contraintes de clés étrangères (Foreign Keys)
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Création de la base de données...");
        executeSqlScript(db, "db.sql");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Mise à jour de la base de données de " + oldVersion + " vers " + newVersion);
        // Pour le développement : on supprime et on recrée
        // En production, il faudrait des scripts de migration ALTER TABLE...
        dropAllTables(db);
        onCreate(db);
    }

    private void dropAllTables(SQLiteDatabase db) {
        // Suppression brutale pour le dev (ordre inverse des dépendances ou via script)
        // Ici on relance juste la création, SQLite gère les erreurs si tables existent
        // pas
        // Mieux : Lister les tables et les dropper
        // Pour simplifier ici, on suppose que le développeur désinstalle l'app ou
        // cleardata
        // Ou on peut exécuter un script "clean.sql" si on en avait un.

        // Méthode simple :
        String[] tables = { "notifications", "participants", "cours", "certifications_professionnelles",
                "demandes_certificats", "notes", "inscriptions", "modules",
                "admins", "etudiants", "professeurs", "users", "sessions" };
        for (String table : tables) {
            db.execSQL("DROP TABLE IF EXISTS " + table);
        }

        // Supprimer les vues
        String[] views = { "vue_professeurs", "vue_etudiants", "vue_notes_completes",
                "vue_demandes_completes", "vue_stats_etudiants" };
        for (String view : views) {
            db.execSQL("DROP VIEW IF EXISTS " + view);
        }
    }

    private void executeSqlScript(SQLiteDatabase db, String fileName) {
        try {
            InputStream is = context.getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder statement = new StringBuilder();
            boolean insideTrigger = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Ignorer les lignes vides et les commentaires
                if (line.isEmpty() || line.startsWith("--")) {
                    continue;
                }

                statement.append(line).append(" ");

                // Détection simplifiée des Triggers
                if (line.toUpperCase().startsWith("CREATE TRIGGER")) {
                    insideTrigger = true;
                }

                if (line.endsWith(";")) {
                    if (insideTrigger) {
                        // Si on est dans un trigger, on attend la fin "END;"
                        if (line.toUpperCase().endsWith("END;")) {
                            executeStatement(db, statement.toString());
                            statement = new StringBuilder();
                            insideTrigger = false;
                        }
                    } else {
                        // Cas normal
                        executeStatement(db, statement.toString());
                        statement = new StringBuilder();
                    }
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Erreur de lecture du fichier script: " + fileName, e);
        }
    }

    private void executeStatement(SQLiteDatabase db, String sql) {
        try {
            db.execSQL(sql);
            Log.d(TAG, "Exécuté: " + (sql.length() > 50 ? sql.substring(0, 50) + "..." : sql));
        } catch (SQLException e) {
            Log.e(TAG, "Erreur SQL: " + e.getMessage());
            Log.e(TAG, "Requête en échec: " + sql);
        }
    }
}
