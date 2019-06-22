package com.test.admin.servicedesk.ui.main.my_requests;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.test.admin.servicedesk.BaseApplication;
import com.test.admin.servicedesk.Callback;
import com.test.admin.servicedesk.NotificationHelper;
import com.test.admin.servicedesk.R;
import com.test.admin.servicedesk.models.NewProgressRequestsPage;
import com.test.admin.servicedesk.models.RequestsPage;
import com.test.admin.servicedesk.models.User;
import com.test.admin.servicedesk.network.main.RequestsApi;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;

public class MyRequestsFragmentViewModel extends ViewModel {

    private static final String TAG = "MyRequestsFragmentVM";

    RequestsApi requestsApi;

    @Inject
    public MyRequestsFragmentViewModel(RequestsApi requestsApi) {
        this.requestsApi = requestsApi;
    }

    /**
     * Get user requests in {@link MyRequestsFragment}
     * @param emp_id    current user id from {@link com.test.admin.servicedesk.dependencyinjection.app.SessionManager}
     * @param page      page number, cause loading happens in pages
     * @param status_id status id of requests user wanna get
     */

    public void get_my_requests(int emp_id, int page, int status_id, Callback callback){
        Log.d(TAG, "get_my_requests: called");
        Log.d(TAG, "get_my_requests: " + requestsApi.getMyRequests(emp_id, page, status_id).request().url().toString());
        requestsApi.getMyRequests(emp_id, page, status_id).enqueue(new retrofit2.Callback<RequestsPage>() {
            @Override
            public void onResponse(Call<RequestsPage> call, Response<RequestsPage> response) {
                Log.d(TAG, "onResponse: called");
                User.myRequestsAmount = response.body().getRequests().length;
                callback.result(response.body());
            }

            @Override
            public void onFailure(Call<RequestsPage> call, Throwable t) {
                Log.d(TAG, "onFailure: called " + t.getMessage());
                callback.result(null);
            }
        });
    }

    public void get_new_progress_requests_amount(int emp_id){
        Log.d(TAG, "get_new_progress_requests_amount: called");
        Log.d(TAG, "get_new_progress_requests_amount: " + requestsApi.getNewProgressRequests(emp_id).request().url().toString());
        requestsApi.getNewProgressRequests(emp_id).enqueue(new retrofit2.Callback<NewProgressRequestsPage>() {
            @Override
            public void onResponse(Call<NewProgressRequestsPage> call, Response<NewProgressRequestsPage> response) {
                Log.d(TAG, "onResponse: called");
                Log.d(TAG, "onResponse: " + response.body().getAll_requests_amount());
                saveUserRequestsAmount(response.body().getAll_requests_amount());
            }

            @Override
            public void onFailure(Call<NewProgressRequestsPage> call, Throwable t) {
                Log.d(TAG, "onFailure: called");
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    /**
     * Put user requests amount to SharedPreferences
     * @param amount    user requests amount
     */

    private void saveUserRequestsAmount(int amount){
        SharedPreferences sharedPreferences = BaseApplication.getContext().getSharedPreferences(BaseApplication.getContext().getString(R.string.settings), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(BaseApplication.getContext().getString(R.string.userRequestsAmount), amount);
        editor.apply();
    }

}
