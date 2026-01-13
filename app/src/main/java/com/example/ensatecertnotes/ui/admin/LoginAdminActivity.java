package com.example.ensatecertnotes.ui.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ensatecertnotes.R;
import com.example.ensatecertnotes.db.DatabaseHelper;

public class LoginAdminActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        // Initialisation
        dbHelper = DatabaseHelper.getInstance(this);
        sharedPreferences = getSharedPreferences("AdminSession", MODE_PRIVATE);

        // Vérifier si déjà connecté
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            navigateToDashboard();
            return;
        }

        etEmail = findViewById(R.id.et_email_admin);
        etPassword = findViewById(R.id.et_password_admin);
        btnLogin = findViewById(R.id.btn_login_admin);

        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        String query = "SELECT u.id, u.role, a.id as admin_id, a.nom, a.prenom, a.fonction " +
                      "FROM users u " +
                      "INNER JOIN admins a ON u.id = a.user_id " +
                      "WHERE u.email = ? AND u.password = ? AND (u.role = 'AGENT' OR u.role = 'MANAGER')";
        
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(0);
            String role = cursor.getString(1);
            int adminId = cursor.getInt(2);
            String nom = cursor.getString(3);
            String prenom = cursor.getString(4);
            String fonction = cursor.getString(5);

            // Sauvegarder la session
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.putInt("userId", userId);
            editor.putInt("adminId", adminId);
            editor.putString("email", email);
            editor.putString("nom", nom);
            editor.putString("prenom", prenom);
            editor.putString("fonction", fonction);
            editor.putString("role", role);
            editor.apply();

            Toast.makeText(this, "Bienvenue " + prenom + " " + nom, Toast.LENGTH_SHORT).show();
            navigateToDashboard();
        } else {
            Toast.makeText(this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(LoginAdminActivity.this, DashboardAdminActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}