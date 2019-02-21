package com.example.admin.oracletest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


/**
 * Настройки
 */

public class Settings {

    private static final String TAG = "Settings";

    private static String USER_PASSWORD = "user_password";
    private static String USER_LOGIN = "user_login";
    private static final String SETTINGS = "settings";
    private static SharedPreferences sharedPreferences;

    static {
        Context context = DirectoryServiceDesk.GetAppContext();
        sharedPreferences = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
    }

    public static void setUserLogin(String userLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_LOGIN, userLogin);
        editor.apply();
    }

    public static void setUserPassword(String userPassword) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_PASSWORD, userPassword);
        editor.apply();
    }

    public static String getUserLogin() {
        return sharedPreferences.getString(USER_LOGIN, "");
    }

    public static String getUserPassword() {
        return sharedPreferences.getString(USER_PASSWORD, "");
    }
}
