package com.test.admin.servicedesk.Models;

import android.util.Log;

import com.test.admin.servicedesk.Settings;

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
    public static int search_requests_amount;

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
