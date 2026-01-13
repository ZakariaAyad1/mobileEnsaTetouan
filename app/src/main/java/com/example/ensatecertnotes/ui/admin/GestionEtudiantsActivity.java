package com.example.ensatecertnotes.ui.admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.DatabaseHelper;
import com.example.ensatecertnotes.ui.admin.adapters.EtudiantAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.HashMap;

public class GestionEtudiantsActivity extends AppCompatActivity {

    private EditText etSearch;
    private RecyclerView recyclerView;
    private EtudiantAdapter adapter;
    private DatabaseHelper dbHelper;
    private BottomNavigationView bottomNav;
    private FloatingActionButton fabAddEtudiant;
    private ArrayList<HashMap<String, String>> etudiantsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_etudiants);

        dbHelper = DatabaseHelper.getInstance(this);
        etudiantsList = new ArrayList<>();

        // Initialisation
        etSearch = findViewById(R.id.et_search_etudiant);
        recyclerView = findViewById(R.id.recycler_etudiants);
        bottomNav = findViewById(R.id.bottom_nav_admin);
        fabAddEtudiant = findViewById(R.id.fab_add_etudiant);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EtudiantAdapter(this, etudiantsList, this::showEtudiantDetails);
        recyclerView.setAdapter(adapter);

        // Bouton Ajouter
        fabAddEtudiant.setOnClickListener(v -> showAddEtudiantDialog());

        // Recherche
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchEtudiants(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Bottom Navigation
        bottomNav.setSelectedItemId(R.id.nav_etudiants);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_dashboard) {
                startActivity(new Intent(this, DashboardAdminActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_demandes) {
                startActivity(new Intent(this, ListeDemandesActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_etudiants) {
                return true;
            } else if (itemId == R.id.nav_profil) {
                startActivity(new Intent(this, ProfilAdminActivity.class));
                finish();
                return true;
            }
            return false;
        });

        loadAllEtudiants();
    }

    private void loadAllEtudiants() {
        etudiantsList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT e.id, e.cne, e.nom, e.prenom, e.filiere, e.annee_etude, " +
                      "e.date_naissance, e.lieu_naissance, u.email, e.user_id, " +
                      "(SELECT COUNT(*) FROM notes WHERE etudiant_id = e.id) as nb_notes, " +
                      "(SELECT AVG(note_finale) FROM notes WHERE etudiant_id = e.id) as moyenne " +
                      "FROM etudiants e " +
                      "INNER JOIN users u ON e.user_id = u.id " +
                      "ORDER BY e.nom, e.prenom";

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            HashMap<String, String> etudiant = new HashMap<>();
            etudiant.put("id", cursor.getString(0));
            etudiant.put("cne", cursor.getString(1));
            etudiant.put("nom", cursor.getString(2));
            etudiant.put("prenom", cursor.getString(3));
            etudiant.put("filiere", cursor.getString(4));
            etudiant.put("annee", cursor.getString(5));
            etudiant.put("date_naissance", cursor.getString(6) != null ? cursor.getString(6) : "");
            etudiant.put("lieu_naissance", cursor.getString(7) != null ? cursor.getString(7) : "");
            etudiant.put("email", cursor.getString(8));
            etudiant.put("user_id", cursor.getString(9));
            etudiant.put("nb_notes", cursor.getString(10));
            
            double moyenne = cursor.isNull(11) ? 0 : cursor.getDouble(11);
            etudiant.put("moyenne", String.format("%.2f", moyenne));
            
            etudiantsList.add(etudiant);
        }
        cursor.close();

        adapter.notifyDataSetChanged();
    }

    private void searchEtudiants(String query) {
        if (query.isEmpty()) {
            loadAllEtudiants();
            return;
        }

        etudiantsList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sqlQuery = "SELECT e.id, e.cne, e.nom, e.prenom, e.filiere, e.annee_etude, " +
                         "e.date_naissance, e.lieu_naissance, u.email, e.user_id, " +
                         "(SELECT COUNT(*) FROM notes WHERE etudiant_id = e.id) as nb_notes, " +
                         "(SELECT AVG(note_finale) FROM notes WHERE etudiant_id = e.id) as moyenne " +
                         "FROM etudiants e " +
                         "INNER JOIN users u ON e.user_id = u.id " +
                         "WHERE e.cne LIKE ? OR e.nom LIKE ? OR e.prenom LIKE ? " +
                         "ORDER BY e.nom, e.prenom";

        String searchPattern = "%" + query + "%";
        Cursor cursor = db.rawQuery(sqlQuery, new String[]{searchPattern, searchPattern, searchPattern});

        while (cursor.moveToNext()) {
            HashMap<String, String> etudiant = new HashMap<>();
            etudiant.put("id", cursor.getString(0));
            etudiant.put("cne", cursor.getString(1));
            etudiant.put("nom", cursor.getString(2));
            etudiant.put("prenom", cursor.getString(3));
            etudiant.put("filiere", cursor.getString(4));
            etudiant.put("annee", cursor.getString(5));
            etudiant.put("date_naissance", cursor.getString(6) != null ? cursor.getString(6) : "");
            etudiant.put("lieu_naissance", cursor.getString(7) != null ? cursor.getString(7) : "");
            etudiant.put("email", cursor.getString(8));
            etudiant.put("user_id", cursor.getString(9));
            etudiant.put("nb_notes", cursor.getString(10));
            
            double moyenne = cursor.isNull(11) ? 0 : cursor.getDouble(11);
            etudiant.put("moyenne", String.format("%.2f", moyenne));
            
            etudiantsList.add(etudiant);
        }
        cursor.close();

        adapter.notifyDataSetChanged();
    }

    private void showEtudiantDetails(HashMap<String, String> etudiant) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_etudiant_details);
        dialog.getWindow().setLayout(
            (int)(getResources().getDisplayMetrics().widthPixels * 0.95),
            (int)(getResources().getDisplayMetrics().heightPixels * 0.85)
        );

        // Infos générales
        TextView tvCNE = dialog.findViewById(R.id.tv_detail_cne);
        TextView tvNom = dialog.findViewById(R.id.tv_detail_nom);
        TextView tvEmail = dialog.findViewById(R.id.tv_detail_email);
        TextView tvFiliere = dialog.findViewById(R.id.tv_detail_filiere);
        TextView tvAnnee = dialog.findViewById(R.id.tv_detail_annee);
        TextView tvDateNaissance = dialog.findViewById(R.id.tv_detail_date_naissance);
        TextView tvLieuNaissance = dialog.findViewById(R.id.tv_detail_lieu_naissance);
        TextView tvMoyenne = dialog.findViewById(R.id.tv_detail_moyenne);
        TextView tvNbNotes = dialog.findViewById(R.id.tv_detail_nb_notes);
        
        // RecyclerView pour les notes
        RecyclerView recyclerNotes = dialog.findViewById(R.id.recycler_notes_etudiant);
        recyclerNotes.setLayoutManager(new LinearLayoutManager(this));

        // Boutons d'action
        Button btnEdit = dialog.findViewById(R.id.btn_edit_etudiant);
        Button btnDelete = dialog.findViewById(R.id.btn_delete_etudiant);
        Button btnClose = dialog.findViewById(R.id.btn_close_dialog);

        // Afficher les infos
        tvCNE.setText("CNE: " + etudiant.get("cne"));
        tvNom.setText(etudiant.get("nom") + " " + etudiant.get("prenom"));
        tvEmail.setText("Email: " + etudiant.get("email"));
        tvFiliere.setText("Filière: " + etudiant.get("filiere"));
        tvAnnee.setText("Année: " + etudiant.get("annee"));
        tvDateNaissance.setText("Date naissance: " + etudiant.get("date_naissance"));
        tvLieuNaissance.setText("Lieu naissance: " + etudiant.get("lieu_naissance"));
        tvMoyenne.setText("Moyenne générale: " + etudiant.get("moyenne") + " / 20");
        tvNbNotes.setText("Modules évalués: " + etudiant.get("nb_notes"));

        // Charger les notes par module
        ArrayList<HashMap<String, String>> notesList = loadNotesEtudiant(etudiant.get("id"));
        NotesAdapter notesAdapter = new NotesAdapter(this, notesList);
        recyclerNotes.setAdapter(notesAdapter);

        // Actions
        btnEdit.setOnClickListener(v -> {
            dialog.dismiss();
            showEditEtudiantDialog(etudiant);
        });

        btnDelete.setOnClickListener(v -> {
            dialog.dismiss();
            confirmDeleteEtudiant(etudiant);
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private ArrayList<HashMap<String, String>> loadNotesEtudiant(String etudiantId) {
        ArrayList<HashMap<String, String>> notesList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT m.code_module, m.nom_module, m.semestre, " +
                      "n.note_examen, n.note_td, n.note_tp, n.note_finale, n.statut " +
                      "FROM notes n " +
                      "INNER JOIN modules m ON n.module_id = m.id " +
                      "WHERE n.etudiant_id = ? " +
                      "ORDER BY m.semestre, m.nom_module";

        Cursor cursor = db.rawQuery(query, new String[]{etudiantId});
        while (cursor.moveToNext()) {
            HashMap<String, String> note = new HashMap<>();
            note.put("code_module", cursor.getString(0));
            note.put("nom_module", cursor.getString(1));
            note.put("semestre", "S" + cursor.getString(2));
            note.put("note_examen", cursor.isNull(3) ? "-" : String.format("%.2f", cursor.getDouble(3)));
            note.put("note_td", cursor.isNull(4) ? "-" : String.format("%.2f", cursor.getDouble(4)));
            note.put("note_tp", cursor.isNull(5) ? "-" : String.format("%.2f", cursor.getDouble(5)));
            note.put("note_finale", cursor.isNull(6) ? "-" : String.format("%.2f", cursor.getDouble(6)));
            note.put("statut", cursor.getString(7));
            notesList.add(note);
        }
        cursor.close();

        return notesList;
    }

    private void showAddEtudiantDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_etudiant, null);
        builder.setView(dialogView);

        EditText etCNE = dialogView.findViewById(R.id.et_dialog_cne);
        EditText etNom = dialogView.findViewById(R.id.et_dialog_nom);
        EditText etPrenom = dialogView.findViewById(R.id.et_dialog_prenom);
        EditText etEmail = dialogView.findViewById(R.id.et_dialog_email);
        EditText etPassword = dialogView.findViewById(R.id.et_dialog_password);
        EditText etDateNaissance = dialogView.findViewById(R.id.et_dialog_date_naissance);
        EditText etLieuNaissance = dialogView.findViewById(R.id.et_dialog_lieu_naissance);
        Spinner spinnerFiliere = dialogView.findViewById(R.id.spinner_filiere);
        Spinner spinnerAnnee = dialogView.findViewById(R.id.spinner_annee);

        // Remplir les spinners
        ArrayAdapter<CharSequence> filiereAdapter = ArrayAdapter.createFromResource(this,
                R.array.filieres_array, android.R.layout.simple_spinner_item);
        filiereAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiliere.setAdapter(filiereAdapter);

        ArrayAdapter<CharSequence> anneeAdapter = ArrayAdapter.createFromResource(this,
                R.array.annees_array, android.R.layout.simple_spinner_item);
        anneeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnnee.setAdapter(anneeAdapter);

        builder.setTitle("Ajouter un étudiant")
               .setPositiveButton("Ajouter", null)
               .setNegativeButton("Annuler", (d, w) -> d.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String cne = etCNE.getText().toString().trim();
            String nom = etNom.getText().toString().trim();
            String prenom = etPrenom.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String dateNaissance = etDateNaissance.getText().toString().trim();
            String lieuNaissance = etLieuNaissance.getText().toString().trim();
            String filiere = spinnerFiliere.getSelectedItem().toString();
            String annee = spinnerAnnee.getSelectedItem().toString();

            if (cne.isEmpty() || nom.isEmpty() || prenom.isEmpty() || 
                email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs obligatoires", 
                              Toast.LENGTH_SHORT).show();
                return;
            }

            if (addEtudiant(cne, nom, prenom, email, password, dateNaissance, 
                           lieuNaissance, filiere, annee)) {
                Toast.makeText(this, "Étudiant ajouté avec succès", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                loadAllEtudiants();
            } else {
                Toast.makeText(this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditEtudiantDialog(HashMap<String, String> etudiant) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_etudiant, null);
        builder.setView(dialogView);

        EditText etCNE = dialogView.findViewById(R.id.et_dialog_cne);
        EditText etNom = dialogView.findViewById(R.id.et_dialog_nom);
        EditText etPrenom = dialogView.findViewById(R.id.et_dialog_prenom);
        EditText etEmail = dialogView.findViewById(R.id.et_dialog_email);
        EditText etPassword = dialogView.findViewById(R.id.et_dialog_password);
        EditText etDateNaissance = dialogView.findViewById(R.id.et_dialog_date_naissance);
        EditText etLieuNaissance = dialogView.findViewById(R.id.et_dialog_lieu_naissance);
        Spinner spinnerFiliere = dialogView.findViewById(R.id.spinner_filiere);
        Spinner spinnerAnnee = dialogView.findViewById(R.id.spinner_annee);

        // Remplir les spinners
        ArrayAdapter<CharSequence> filiereAdapter = ArrayAdapter.createFromResource(this,
                R.array.filieres_array, android.R.layout.simple_spinner_item);
        filiereAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiliere.setAdapter(filiereAdapter);

        ArrayAdapter<CharSequence> anneeAdapter = ArrayAdapter.createFromResource(this,
                R.array.annees_array, android.R.layout.simple_spinner_item);
        anneeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnnee.setAdapter(anneeAdapter);

        // Pré-remplir avec les données existantes
        etCNE.setText(etudiant.get("cne"));
        etCNE.setEnabled(false); // CNE non modifiable
        etNom.setText(etudiant.get("nom"));
        etPrenom.setText(etudiant.get("prenom"));
        etEmail.setText(etudiant.get("email"));
        etPassword.setHint("Laisser vide pour ne pas changer");
        etDateNaissance.setText(etudiant.get("date_naissance"));
        etLieuNaissance.setText(etudiant.get("lieu_naissance"));

        // Sélectionner la filière
        int filierePosition = filiereAdapter.getPosition(etudiant.get("filiere"));
        if (filierePosition >= 0) spinnerFiliere.setSelection(filierePosition);

        // Sélectionner l'année
        int anneePosition = anneeAdapter.getPosition(etudiant.get("annee"));
        if (anneePosition >= 0) spinnerAnnee.setSelection(anneePosition);

        builder.setTitle("Modifier l'étudiant")
               .setPositiveButton("Enregistrer", null)
               .setNegativeButton("Annuler", (d, w) -> d.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String nom = etNom.getText().toString().trim();
            String prenom = etPrenom.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String dateNaissance = etDateNaissance.getText().toString().trim();
            String lieuNaissance = etLieuNaissance.getText().toString().trim();
            String filiere = spinnerFiliere.getSelectedItem().toString();
            String annee = spinnerAnnee.getSelectedItem().toString();

            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs obligatoires", 
                              Toast.LENGTH_SHORT).show();
                return;
            }

            if (updateEtudiant(etudiant.get("id"), etudiant.get("user_id"), nom, prenom, 
                             email, password, dateNaissance, lieuNaissance, filiere, annee)) {
                Toast.makeText(this, "Étudiant modifié avec succès", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                loadAllEtudiants();
            } else {
                Toast.makeText(this, "Erreur lors de la modification", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmDeleteEtudiant(HashMap<String, String> etudiant) {
        new AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Êtes-vous sûr de vouloir supprimer " + 
                       etudiant.get("prenom") + " " + etudiant.get("nom") + " ?\n\n" +
                       "⚠️ Toutes les notes et demandes seront également supprimées.")
            .setPositiveButton("Supprimer", (d, w) -> {
                if (deleteEtudiant(etudiant.get("user_id"))) {
                    Toast.makeText(this, "Étudiant supprimé", Toast.LENGTH_SHORT).show();
                    loadAllEtudiants();
                } else {
                    Toast.makeText(this, "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Annuler", null)
            .show();
    }

    private boolean addEtudiant(String cne, String nom, String prenom, String email, 
                               String password, String dateNaissance, String lieuNaissance,
                               String filiere, String annee) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            // Créer l'utilisateur
            ContentValues userValues = new ContentValues();
            userValues.put("email", email);
            userValues.put("password", password); // TODO: Hasher le password
            userValues.put("role", "ETUDIANT");
            long userId = db.insert("users", null, userValues);

            if (userId == -1) return false;

            // Créer l'étudiant
            ContentValues etudiantValues = new ContentValues();
            etudiantValues.put("user_id", userId);
            etudiantValues.put("cne", cne);
            etudiantValues.put("nom", nom);
            etudiantValues.put("prenom", prenom);
            etudiantValues.put("date_naissance", dateNaissance);
            etudiantValues.put("lieu_naissance", lieuNaissance);
            etudiantValues.put("filiere", filiere);
            etudiantValues.put("annee_etude", annee);
            
            long etudiantId = db.insert("etudiants", null, etudiantValues);
            
            if (etudiantId == -1) {
                db.endTransaction();
                return false;
            }

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }
    }

    private boolean updateEtudiant(String etudiantId, String userId, String nom, String prenom, 
                                  String email, String password, String dateNaissance, 
                                  String lieuNaissance, String filiere, String annee) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            // Mettre à jour l'utilisateur
            ContentValues userValues = new ContentValues();
            userValues.put("email", email);
            if (!password.isEmpty()) {
                userValues.put("password", password); // TODO: Hasher le password
            }
            
            int userUpdate = db.update("users", userValues, "id = ?", new String[]{userId});

            // Mettre à jour l'étudiant
            ContentValues etudiantValues = new ContentValues();
            etudiantValues.put("nom", nom);
            etudiantValues.put("prenom", prenom);
            etudiantValues.put("date_naissance", dateNaissance);
            etudiantValues.put("lieu_naissance", lieuNaissance);
            etudiantValues.put("filiere", filiere);
            etudiantValues.put("annee_etude", annee);
            
            int etudiantUpdate = db.update("etudiants", etudiantValues, 
                                          "id = ?", new String[]{etudiantId});

            if (userUpdate > 0 && etudiantUpdate > 0) {
                db.setTransactionSuccessful();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }
    }

    private boolean deleteEtudiant(String userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Grâce à ON DELETE CASCADE, toutes les données liées seront supprimées
        int result = db.delete("users", "id = ?", new String[]{userId});
        return result > 0;
    }

    // Adapter interne pour afficher les notes
    private class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
        private android.content.Context context;
        private ArrayList<HashMap<String, String>> notes;

        NotesAdapter(android.content.Context ctx, ArrayList<HashMap<String, String>> list) {
            context = ctx;
            notes = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_note_module, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            HashMap<String, String> note = notes.get(position);
            
            holder.tvModule.setText(note.get("nom_module"));
            holder.tvCode.setText(note.get("code_module") + " - " + note.get("semestre"));
            holder.tvExamen.setText("Examen: " + note.get("note_examen"));
            holder.tvTD.setText("TD: " + note.get("note_td"));
            holder.tvTP.setText("TP: " + note.get("note_tp"));
            holder.tvFinale.setText("Note finale: " + note.get("note_finale") + "/20");
            holder.tvStatut.setText(note.get("statut"));
            
            // Couleur selon le statut
            String statut = note.get("statut");
            if ("VALIDE".equals(statut)) {
                holder.tvStatut.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else if ("NON_VALIDE".equals(statut)) {
                holder.tvStatut.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            } else {
                holder.tvStatut.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            }
        }

        @Override
        public int getItemCount() {
            return notes.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvModule, tvCode, tvExamen, tvTD, tvTP, tvFinale, tvStatut;

            ViewHolder(View v) {
                super(v);
                tvModule = v.findViewById(R.id.tv_nom_module);
                tvCode = v.findViewById(R.id.tv_code_module);
                tvExamen = v.findViewById(R.id.tv_note_examen);
                tvTD = v.findViewById(R.id.tv_note_td);
                tvTP = v.findViewById(R.id.tv_note_tp);
                tvFinale = v.findViewById(R.id.tv_note_finale);
                tvStatut = v.findViewById(R.id.tv_statut_note);
            }
        }
    }
}