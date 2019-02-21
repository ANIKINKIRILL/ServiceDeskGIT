package com.example.admin.oracletest.Models;

import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.Settings;

import org.json.JSONException;
import org.json.JSONObject;

/**
    * Класс пользователя
 */

public class User {

    private static final String TAG = "User";

    private static boolean isAuthorized; // Авторизован ли пользователь
    private static String login;
    private static String password;
    private static Callback externalCallbackAuth;

    /**
        * Авторизация пользователя
        *
        * @param login              Логин пользователья
        * @param password           Пароль пользователья
        * @param callback           Функция, которая вызывается после получения данных
     */

    public static void authenticate(String login, String password, Callback callback){
        externalCallbackAuth = callback;
        User.login = login;
        User.password = password;
        ServerKFU.authenticateUser(login, password, mCallback);
    }

    /**
     * Callback который вызыиться после получения данных на background thread
     */

    private static Callback mCallback = new Callback() {
        @Override
        public void execute(Object data) {
            try {
                JSONObject jsonObject = new JSONObject(data.toString());
                boolean successful = jsonObject.getBoolean("successful");
                if(successful){
                    isAuthorized = true;
                    saveInformation();
                    externalCallbackAuth.execute(true);
                }else{
                    externalCallbackAuth.execute(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private static void saveInformation(){
        Settings.setUserLogin(login);
        Settings.setUserPassword(password);
    }

}
