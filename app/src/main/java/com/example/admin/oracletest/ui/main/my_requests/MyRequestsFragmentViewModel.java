package com.example.admin.oracletest.ui.main.my_requests;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.models.EmployeeRequest;
import com.example.admin.oracletest.models.RequestsPage;
import com.example.admin.oracletest.models.User;
import com.example.admin.oracletest.network.main.RequestsApi;
import com.example.admin.oracletest.ui.main.GetRequestsResource;

import org.reactivestreams.Subscription;

import javax.inject.Inject;

import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
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
     * @param emp_id    current user id from {@link com.example.admin.oracletest.dependencyinjection.app.SessionManager}
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


}
