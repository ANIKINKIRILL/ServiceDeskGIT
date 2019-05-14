package com.example.admin.oracletest.Models;

import android.util.Log;

import com.example.admin.oracletest.Settings;

/**
 * Класс пользователя
 */

public class User {

    // Лог
    private static final String TAG = "User";

    // Переменные
    public static boolean isAuthorized; // Авторизован ли пользователь
    public static String login;
    public static String password;
    public static int user_id;
    public static String firstname;
    public static String lastname;
    public static String middlename;
    public static int current_requests_amount;

    /**
     * Сохранение данных пользователя в {@link Settings}
     */

    public static void saveInformation(){
        Settings.setUserLogin(login);
        Settings.setUserPassword(password);
        Settings.setUserId(Integer.toString(user_id));
        Settings.setUserFirstName(firstname);
        Settings.setUserMiddleName(middlename);
        Settings.setUserLastName(lastname);
    }

    /**
     * Выход пользователя
     */

    public static void exit(){
        isAuthorized = false;
        Settings.setUserFirstName("");
        Settings.setUserMiddleName("");
        Settings.setUserLastName("");
        Settings.setUserId("");
        Settings.setUserLogin("");
        Settings.setUserPassword("");
    }

}
