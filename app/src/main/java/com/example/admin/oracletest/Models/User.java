package com.example.admin.oracletest.Models;

import android.util.Log;

import com.example.admin.oracletest.Callback;

/**
    * Класс пользователя
 */

public class User {
    private static boolean isAuthorized;
    private String login;
    private String password;
    private static Callback callback;

    /**
        * Авторизация пользователя
        *
        * @param login              Логин пользователья
        * @param password           Пароль пользователья
        * @param callback           Функция, которая вызывается после получения данных
     */

    public static void authenticate(String login, String password, Callback callback){
        ServerKFU.authenticateUser(login, password, callback);
    }

}
