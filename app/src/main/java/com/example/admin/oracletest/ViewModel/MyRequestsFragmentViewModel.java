package com.example.admin.oracletest.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.Fragment.MyRequestsFragment;
import com.example.admin.oracletest.GetDataFromKfuServer;
import com.example.admin.oracletest.Models.EmployeeRequest;
import com.example.admin.oracletest.Models.User;
import com.example.admin.oracletest.Repository.Repository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * ViewModel для {@link MyRequestsFragment}
 * Здесь представлена вся основная бизнес логика для MyRequestsFragment.
 * Все заявки на пользователя
 */

public class MyRequestsFragmentViewModel extends ViewModel {

    private static final String TAG = "MyRequestsFragmentViewM";
    private static Callback externalGetRequestsCallback;

    /**
     * Получить заявки исполнителя
     * @param callback Callback, который вернется после получения заявок на исполнителя
     * @param u_id     id исполнителя
     */

    public void get_requests(Context context, String u_id, int page_number, int status_id, Callback callback){
        Log.d(TAG, "get_requests: called");
        externalGetRequestsCallback = callback;
        Repository.getInstance(context).get_requests(u_id, page_number, status_id, mGetRequestsCallback);
    }

    /**
     * Callback, который вернеться после получения JSON
     * {@link GetDataFromKfuServer}
     * Парсинг JSON. Все запросы на исполнителя с id = u_id в User.get_requests
     * При завершении парсинга, собираем все в список и
     * передаем на {@link MyRequestsFragment} callback
     */

    private static Callback mGetRequestsCallback = new Callback() {
        @Override
        public void execute(Object data) {
            ArrayList<EmployeeRequest> requests = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(data.toString());
                boolean successful = jsonObject.getBoolean("successful");
                User.current_requests_amount = jsonObject.getInt("v_emp_requests_amount");
                if(successful){
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
                }
            } catch (JSONException e) {
                Log.d(TAG, "execute: " + e.getMessage());
                e.printStackTrace();
            }
            MutableLiveData<ArrayList<EmployeeRequest>> mutableLiveData = new MutableLiveData<>();
            mutableLiveData.setValue(requests);
            externalGetRequestsCallback.execute(mutableLiveData);
        }
    };

}
