package com.test.admin.servicedesk.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.test.admin.servicedesk.Callback;
import com.test.admin.servicedesk.Models.EmployeeRequest;
import com.test.admin.servicedesk.Models.User;
import com.test.admin.servicedesk.Repository.Repository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *  ViewModel для {@link com.test.admin.servicedesk.Fragment.SearchRequestsFragment},
 *  {@link com.test.admin.servicedesk.Fragment.ViewSearchRequestsFragment}
 */

public class SearchRequestsFragmentViewModel extends ViewModel {

    private static final String TAG = "SearchRequestsFragVM";

    private static Callback externalCallback;

    /**
     * Получить результаты поиска
     * @param p_sql_statement   SQL запрос на сервер
     * @param p_sql_statement_count_rows    SQL запрос на сервер для подсчета найденных строк
     * @param p_page_number     номер страницы с заявками
     * @param callback          callback с {@link com.test.admin.servicedesk.Fragment.SearchRequestsFragment}
     */

    public void search_request(String p_sql_statement, String p_sql_statement_count_rows, int p_page_number, Callback callback, Context context){
        externalCallback = callback;
        Repository.getInstance(context).search_request(p_sql_statement, p_sql_statement_count_rows, p_page_number, mSearchRequestCallback);
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
                User.search_requests_amount = jsonObject.getInt("requests_amount");
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