package com.example.admin.oracletest.Models;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.admin.oracletest.Activity.AuthActivity;
import com.example.admin.oracletest.Activity.MainActivity;
import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.Settings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
    * Класс пользователя
 */

public class User {

    // Logs
    private static final String TAG = "User";

    // vars
    private static boolean isAuthorized; // Авторизован ли пользователь
    private static String login;
    private static String password;
    private static Callback externalCallbackGetRequests;
    private static int user_id;
    private static ArrayList<EmployeeRequest> requests = new ArrayList<>();
    private static String firstname;
    private static String lastname;
    private static String middlename;


    /**
        * Авторизация пользователя
        *
        * @param login              Логин пользователья
        * @param password           Пароль пользователья
     */

    public static void authenticate(String login, String password, Callback callback){
        User.login = login;
        User.password = password;
        // Создаем observable object, который вернет json
        Observable<String> authObservable = Observable.fromCallable(()-> ServerKFU.authenticateUser(login, password))
        // Весь процесс просиходит на Background Thread
        .subscribeOn(Schedulers.io())
        // Вывод данных будет на UI Thread
        .observeOn(AndroidSchedulers.mainThread());
        // Подписываем observer, чтобы на любое изменение
        // UI был обновленным
        authObservable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                makeOutAuthenticationJson(s, callback);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * Разбираем JSON, который вернется после получения ответа с Сервера
     * с данными вошёл ли пользователь в систему или вход завершен ошибкой
     *
     * @param data          JSON
     * @param callback      callback, который будет возвращатьс на AuthActivity полсе
     *                      того как получили результат
     */

    private static void makeOutAuthenticationJson(Object data, Callback callback){
        try {
            JSONObject jsonObject = new JSONObject(data.toString());
            boolean successful = jsonObject.getBoolean("successful");
            if(successful){
                user_id = jsonObject.getInt("user_id");
                firstname = jsonObject.getString("firstname");
                lastname = jsonObject.getString("lastname");
                middlename = jsonObject.getString("middlename");
                isAuthorized = true;
                saveInformation();
                callback.execute(true);
            }else{
                callback.execute(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получить заявки исполнителя
     * @param callback      Callback, который вернется после получения заявок на исполнителя
     * @param u_id          id исполнителя
     */

    public static void get_requests(String u_id, Callback callback){
        externalCallbackGetRequests = callback;
        ServerKFU.get_requests(u_id, mGetRequestsCallback);
    }

    /**
     * Callback, который вернется после получения данных с Сервера КФУ
     */

    private static Callback mGetRequestsCallback = new Callback() {
        @Override
        public void execute(Object data) {
            requests.clear();
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
                    externalCallbackGetRequests.execute(requests);
                }else{
                    externalCallbackGetRequests.execute(requests);
                }
            } catch (JSONException e) {
                Log.d(TAG, "execute: " + e.getMessage());
                e.printStackTrace();
            }


        }
    };

    /**
     * Сохранение логина пароля пользователя в настройках
     */

    private static void saveInformation(){
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
