package com.example.ensatecertnotes.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.ensatecertnotes.ui.auth.LoginActivity;
import com.example.ensatecertnotes.ui.auth.RoleSelectionActivity;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "CertifNotesPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ROLE = "role";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(int userId, String email, String role) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROLE, role);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_ROLE, pref.getString(KEY_ROLE, null));
        return user;
    }

    public int getUserId() {
        return pref.getInt(KEY_USER_ID, -1);
    }

    public String getRole() {
        return pref.getString(KEY_ROLE, null);
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, RoleSelectionActivity.class); // Or LoginActivity
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }
}
