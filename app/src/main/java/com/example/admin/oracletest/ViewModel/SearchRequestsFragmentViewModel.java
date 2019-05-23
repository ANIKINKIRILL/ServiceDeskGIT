package com.example.admin.oracletest.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.Models.EmployeeRequest;
import com.example.admin.oracletest.Models.User;
import com.example.admin.oracletest.Repository.Repository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *  ViewModel для {@link com.example.admin.oracletest.Fragment.SearchRequestsFragment}
 */

public class SearchRequestsFragmentViewModel extends ViewModel {

    private static final String TAG = "SearchRequestsFragVM";

    private static Callback externalCallback;

    /**
     * Получить результаты поиска
     * @param p_sql_statement   SQL запрос на сервер
     * @param p_sql_statement_count_rows    SQL запрос на сервер для подсчета найденных строк
     * @param callback          callback с {@link com.example.admin.oracletest.Fragment.SearchRequestsFragment}
     */

    public void search_request(String p_sql_statement, String p_sql_statement_count_rows, Callback callback, Context context){
        externalCallback = callback;
        Repository.getInstance(context).search_request(p_sql_statement, p_sql_statement_count_rows, mSearchRequestCallback);
    }

    /**
     * Callback, который вернётся после получения данных с сервера КФУ
     */

    private static Callback mSearchRequestCallback = new Callback() {
        @Override
        public void execute(Object data) {
            ArrayList<EmployeeRequest> requests = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(data.toString());
                JSONArray employeeRequests = jsonObject.getJSONArray("requests");
                for (int i = 0; i < employeeRequests.length(); i++) {
                    int id = employeeRequests.getJSONObject(i).getInt("id");
                    String emp_fio = employeeRequests.getJSONObject(i).getString("emp_fio");
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
                            id, emp_fio, image, request_date, date_of_realization, declarant_fio,
                            post, building_kfu_name, room_number, descr, status_name, color,
                            phone, cod, info
                    );
                    requests.add(request);
                }
            } catch (JSONException e) {
                Log.d(TAG, "execute: " + e.getMessage());
                e.printStackTrace();
            }
            MutableLiveData<ArrayList<EmployeeRequest>> mutableLiveData = new MutableLiveData<>();
            mutableLiveData.setValue(requests);
            externalCallback.execute(mutableLiveData);
        }
    };

}
