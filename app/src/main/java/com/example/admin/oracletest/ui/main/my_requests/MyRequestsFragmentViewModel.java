package com.example.admin.oracletest.ui.main.my_requests;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

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

    public LiveData<GetRequestsResource<RequestsPage>> get_my_requests(int emp_id, int page, int status_id){
        return LiveDataReactiveStreams.fromPublisher(
                requestsApi.getMyRequests(emp_id, page, status_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, RequestsPage>() {
                    @Override
                    public RequestsPage apply(Throwable throwable) throws Exception {
                        RequestsPage errorRequestPage = new RequestsPage();
                        errorRequestPage.setRequests(new EmployeeRequest[] {});
                        return errorRequestPage;
                    }
                })
                .map(new Function<RequestsPage, GetRequestsResource<RequestsPage>>() {
                    @Override
                    public GetRequestsResource<RequestsPage> apply(RequestsPage requestsPage) throws Exception {
                        if(requestsPage.getRequests().length == 0){
                            Log.d(TAG, "apply: error");
                            return GetRequestsResource.error("No data found", requestsPage);
                        }
                        Log.d(TAG, "apply: success");
                        User.myRequestsAmount = requestsPage.getRequests().length;
                        return GetRequestsResource.success(requestsPage);
                    }
                })
        );
    }


}
