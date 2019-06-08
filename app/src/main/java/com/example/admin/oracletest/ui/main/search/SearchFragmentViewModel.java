package com.example.admin.oracletest.ui.main.search;

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
import com.example.admin.oracletest.ui.main.my_requests.MyRequestsFragment;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Response;

public class SearchFragmentViewModel extends ViewModel {

    private static final String TAG = "SearchFragmentViewModel";

    // Injections
    private RequestsApi requestsApi;

    @Inject
    public SearchFragmentViewModel(RequestsApi requestsApi){
        Log.d(TAG, "SearchFragmentViewModel: viewmodel is working...");
        this.requestsApi = requestsApi;
    }

    /**
     * Get searched requests in {@link SearchFragment}
     * @param sql_stm   sql statement
     * @param sql_stm_rows_count    sql statement to get how many rows were effected
     * @param page_number   page
     */

    public void searchRequests(String sql_stm, String sql_stm_rows_count, int page_number, Callback callback){
        Log.d(TAG, "searchRequests: called");
        Log.d(TAG, "searchRequests: " + requestsApi.searchRequests(sql_stm, sql_stm_rows_count, page_number).request().url().toString());
        requestsApi.searchRequests(sql_stm, sql_stm_rows_count, page_number).enqueue(new retrofit2.Callback<RequestsPage>() {
            @Override
            public void onResponse(Call<RequestsPage> call, Response<RequestsPage> response) {
                Log.d(TAG, "onResponse: called");
                User.search_requests_amount = response.body().getRequests_amount();
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
