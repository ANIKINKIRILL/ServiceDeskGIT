package com.test.admin.servicedesk.ui.main.all_requests;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.test.admin.servicedesk.Callback;
import com.test.admin.servicedesk.models.RequestsPage;
import com.test.admin.servicedesk.network.main.RequestsApi;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;

public class AllRequestsFragmentViewModel extends ViewModel {

    private static final String TAG = "AllRequestsFragViewM";

    // Injections
    RequestsApi requestsApi;

    @Inject
    public AllRequestsFragmentViewModel(RequestsApi requestsApi){
        Log.d(TAG, "AllRequestsFragmentViewModel: viewmodel is working...");
        this.requestsApi = requestsApi;
    }

    /**
     * Get requests in {@link AllRequestsFragment}
     * @param page      page number, cause loading happens in pages
     * @param status_id status id of requests user wanna get
     */

    public void get_current_requests(int page, int status_id, Callback callback){
        Log.d(TAG, "get_current_requests: called");
        requestsApi.getCurrentRequests(page, status_id).enqueue(new retrofit2.Callback<RequestsPage>() {
            @Override
            public void onResponse(Call<RequestsPage> call, Response<RequestsPage> response) {
                Log.d(TAG, "onResponse: called");
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
