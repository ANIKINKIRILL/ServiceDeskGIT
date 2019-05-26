package com.test.admin.servicedesk.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.test.admin.servicedesk.Callback;
import com.test.admin.servicedesk.Fragment.AllRequests.AllRequestsFragmentContact;
import com.test.admin.servicedesk.Fragment.MyRequestsFragment;
import com.test.admin.servicedesk.GetDataFromKfuServer;
import com.test.admin.servicedesk.Models.EmployeeRequest;
import com.test.admin.servicedesk.Repository.Repository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllRequestsFragmentViewModel extends ViewModel {

    private static final String TAG = "AllRequestsFragmentView";
    private static Callback externalGetRequestsCallback;
    private AllRequestsFragmentContact.View view;

    /**
     * Получить текущие заявки
     * @param page_number       номер страницы с заявками
     * @param status_id         статус заявки (новая, выполненная и т.д)
     * @param callback          callback для парсинга json после получения текущих заявок c сервера
     * @return                  LiveData из списка с текущими заявками
     */

    public void get_current_requests(Context context, int page_number, int status_id, Callback callback, AllRequestsFragmentContact.View view){
        Log.d(TAG, "get_current_requests: called");
        externalGetRequestsCallback = callback;
        this.view = view;
        Repository.getInstance(context).get_current_requests(page_number, status_id, mGetRequestsCallback);
    }

    /**
     * Callback, который вернеться после получения JSON
     * {@link GetDataFromKfuServer}
     * Парсинг JSON. Все запросы на исполнителя с id = u_id в User.get_requests
     * При завершении парсинга, собираем все в список и
     * передаем на {@link MyRequestsFragment} callback
     */

    private Callback mGetRequestsCallback = new Callback() {
        @Override
        public void execute(Object data) {
            ArrayList<EmployeeRequest> requests = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(data.toString());
                boolean successful = jsonObject.getBoolean("successful");
                if(successful){
                    JSONArray employeeRequests = jsonObject.getJSONArray("requests");
                    for (int i = 0; i < employeeRequests.length(); i++) {
                        int id = employeeRequests.getJSONObject(i).getInt("id");
                        String request_date = employeeRequests.getJSONObject(i).getString("request_date");
                        String emp_fio = employeeRequests.getJSONObject(i).getString("emp_fio");
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
            }
            Log.d(TAG, "execute: " + requests.size());
            MutableLiveData<ArrayList<EmployeeRequest>> mutableLiveData = new MutableLiveData<>();
            mutableLiveData.setValue(requests);
            externalGetRequestsCallback.execute(mutableLiveData);

            if(requests.size() == 0){
                view.userDoesNotHaveRequests();
            }else{
                view.userHasSomeRequests();
            }

        }
    };

}
