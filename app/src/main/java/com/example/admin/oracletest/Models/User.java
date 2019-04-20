package com.example.admin.oracletest.Models;

import android.util.Log;

import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс пользователя
 */

public class User {

    // Лог
    private static final String TAG = "User";

    // Переменные
    private static boolean isAuthorized; // Авторизован ли пользователь
    private static String login;
    private static String password;
    private static int user_id;
    private static String firstname;
    private static String lastname;
    private static String middlename;
    private static Callback externalAuthCallback;
    private static Callback externalGetRequestsCallback;

    /**
    * Авторизация пользователя
    *
    * @param login              Логин пользователья
    * @param password           Пароль пользователья
     */

    public static void authenticate(String login, String password, Callback callback){
        externalAuthCallback = callback;
        User.login = login;
        User.password = password;
        ServerKFU.authenticateUser(login, password, mUserAuthCallback);
    }

    /**
     * Callback, который вернеться после получения JSON
     * {@link com.example.admin.oracletest.GetDataFromKfuServer}
     * Если пользователь успешно авторизован, то отправляем в callback
     * на{@link com.example.admin.oracletest.Activity.AuthActivity} true,
     * иначе false
     */

    private static Callback mUserAuthCallback = new Callback() {
        @Override
        public void execute(Object data) {
            try {
                JSONObject jsonObject = new JSONObject(data.toString());
                boolean successful = jsonObject.getBoolean("successful");
                if(successful){
                    user_id = jsonObject.getInt("user_id");
                    firstname = jsonObject.getString("firstname");
                    middlename = jsonObject.getString("lastname");
                    lastname = jsonObject.getString("middlename");
                    isAuthorized = true;
                    saveInformation();
                    externalAuthCallback.execute(true);
                }else{
                    externalAuthCallback.execute(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * Получить заявки исполнителя
     * @param callback Callback, который вернется после получения заявок на исполнителя
     * @param u_id     id исполнителя
     */

    public static void get_requests(String u_id, Callback callback){
        externalGetRequestsCallback = callback;
        ServerKFU.get_requests(u_id, mGetRequestsCallback);
    }

    /**
     * Callback, который вернеться после получения JSON
     * {@link com.example.admin.oracletest.GetDataFromKfuServer}
     * Парсинг JSON. Все запросы на исполнителя с id = u_id в User.get_requests
     * При завершении парсинга, собираем все в список и
     * передаем на {@link com.example.admin.oracletest.Fragment.MyRequestsFragment} callback
     */

    private static Callback mGetRequestsCallback = new Callback() {
        @Override
        public void execute(Object data) {
            List<EmployeeRequest> requests = new ArrayList<>();
            Log.d(TAG, "json: " + data.toString());
            Log.d(TAG, "word at 849: " + data.toString().substring(845, 850));
            try {
                JSONObject jsonObject = new JSONObject(data.toString());
                boolean successful = jsonObject.getBoolean("successful");
                if(successful){
                    JSONArray employeeRequests = jsonObject.getJSONArray("requests");
                    for (int i = 0; i < employeeRequests.length(); i++) {
                        int id = employeeRequests.getJSONObject(i).getInt("id");
                        String request_date = employeeRequests.getJSONObject(i).getString("request_date");
                        String phone = employeeRequests.getJSONObject(i).getString("phone");
                        String declarant_fio = employeeRequests.getJSONObject(i).getString("declarant_fio");
                        String date_of_realization = employeeRequests.getJSONObject(i).getString("date_of_realization");
                        String post = employeeRequests.getJSONObject(i).getString("post");
                        String info = employeeRequests.getJSONObject(i).getString("info");
                        JSONObject building_kfu = employeeRequests.getJSONObject(i).getJSONObject("building_kfu");
                        String building_kfu_name = building_kfu.getString("name");
                        String room_number = employeeRequests.getJSONObject(i).getString("room_num");
                        JSONObject status = employeeRequests.getJSONObject(i).getJSONObject("status");
                        String status_name = status.getString("status_name");
                        String color = status.getString("color");
                        String descr = status.getString("descr");
                        String image = "";
                        int cod = employeeRequests.getJSONObject(i).getInt("cod");
                        EmployeeRequest request = new EmployeeRequest(
                                id, image, request_date, date_of_realization, declarant_fio,
                                post, building_kfu_name, room_number, descr, status_name, color,
                                phone, cod, info
                        );
                        requests.add(request);
                    }
                }
            } catch (JSONException e) {
                Log.d(TAG, "execute: " + e.getMessage());
                e.printStackTrace();
            }
            externalGetRequestsCallback.execute(requests);
        }
    };

    /**
     * Сохранение данных пользователя в {@link Settings}
     */

    private static void saveInformation(){
        Settings.setUserLogin(login);
        Settings.setUserPassword(password);
        Log.d(TAG, "saveInformation: " + login);
        Log.d(TAG, "saveInformation: " + password);
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
