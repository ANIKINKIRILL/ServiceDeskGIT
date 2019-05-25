package com.test.admin.servicedesk;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Настройки
 */

public class Settings {

    // Logs
    private static final String TAG = "Settings";

    // vars
    public static String USER_PASSWORD = "user_password";
    public static String USER_LOGIN = "user_login";
    public static String USER_ID = "user_id";
    public static String USER_FIRST_NAME = "user_firstname";
    public static String USER_MIDDLE_NAME = "user_middlename";
    public static String USER_LAST_NAME = "user_lastname";
    public static final String SETTINGS = "settings";
    public static SharedPreferences sharedPreferences;

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

    public static void setUserFirstName(String firstName){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_FIRST_NAME, firstName);
        editor.apply();
    }

    public static void setUserMiddleName(String middleName){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_MIDDLE_NAME, middleName);
        editor.apply();
    }

    public static void setUserLastName(String lastName){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_LAST_NAME, lastName);
        editor.apply();
    }

    public static String getUserFirstName() {
        return sharedPreferences.getString(USER_FIRST_NAME, "");
    }

    public static String getUserMiddleName() {
        return sharedPreferences.getString(USER_MIDDLE_NAME, "");
    }

    public static String getUserLastName() {
        return sharedPreferences.getString(USER_LAST_NAME, "");
    }

    public static String getUserId() {
        return sharedPreferences.getString(USER_ID, "");
    }

    public static void setUserId(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, userId);
        editor.apply();
    }

    public static String getUserLogin() {
        return sharedPreferences.getString(USER_LOGIN, "");
    }

    public static String getUserPassword() {
        return sharedPreferences.getString(USER_PASSWORD, "");
    }
}
