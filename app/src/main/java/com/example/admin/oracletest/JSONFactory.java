package com.example.admin.oracletest;

import android.util.Log;

import com.example.admin.oracletest.Models.EmployeeRequest;
import com.example.admin.oracletest.Models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для разбора JSON
 */

public class JSONFactory {

    private static final String TAG = "JSONFactory";

    /**
     * Разбираем JSON, который вернется после получения ответа с Сервера
     * с данными вошёл ли пользователь в систему или вход завершен ошибкой
     *
     * @param data          JSON
     * @param callback      callback, который будет возвращатьс на AuthActivity полсе
     *                      того как получили результат
     */

    public static void makeOutAuthenticateUserJson(Object data, Callback callback){
        try {
            JSONObject jsonObject = new JSONObject(data.toString());
            boolean successful = jsonObject.getBoolean("successful");
            if(successful){
                User.user_id = jsonObject.getInt("user_id");
                User.firstname = jsonObject.getString("firstname");
                User.lastname = jsonObject.getString("lastname");
                User.middlename = jsonObject.getString("middlename");
                User.isAuthorized = true;
                User.saveInformation();
                callback.execute(true);
            }else{
                callback.execute(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Разбираем JSON, который вернется после получения ответа с Сервера
     * с данными вошёл ли пользователь в систему или вход завершен ошибкой
     *
     * @param data          JSON
     * @return              список с запросами на исполнителя
     */

    public static List<EmployeeRequest> makeOutGetEmployeeRequestsJson(Object data){
        List<EmployeeRequest> requests = new ArrayList<>();
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
        return requests;
    }

}
