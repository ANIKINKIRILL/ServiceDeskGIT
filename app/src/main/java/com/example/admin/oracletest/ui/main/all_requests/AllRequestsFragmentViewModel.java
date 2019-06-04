package com.example.admin.oracletest.ui.main.all_requests;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.admin.oracletest.Constants;
import com.example.admin.oracletest.models.RequestsPage;
import com.example.admin.oracletest.network.main.RequestsApi;
import com.example.admin.oracletest.ui.main.GetRequestsResource;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class AllRequestsFragmentViewModel extends ViewModel {

    private static final String TAG = "AllRequestsFragmentViewModel";

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

    public LiveData<GetRequestsResource<RequestsPage>> get_current_requests(int page, int status_id){
        return LiveDataReactiveStreams.fromPublisher(
                requestsApi.getCurrentRequests(page, status_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, RequestsPage>() {
                    @Override
                    public RequestsPage apply(Throwable throwable) throws Exception {
                        RequestsPage errorRequestPage = new RequestsPage();
                        errorRequestPage.setSuccessful(false);
                        return errorRequestPage;
                    }
                })
                .map(new Function<RequestsPage, GetRequestsResource<RequestsPage>>() {
                    @Override
                    public GetRequestsResource<RequestsPage> apply(RequestsPage requestsPage) throws Exception {
                        if(requestsPage.isSuccessful()){
                            return GetRequestsResource.success(requestsPage);
                        }
                        return GetRequestsResource.error(Constants.API_ERROR_WHILE_LOADING, requestsPage);
                    }
                })
        );
    }
}
