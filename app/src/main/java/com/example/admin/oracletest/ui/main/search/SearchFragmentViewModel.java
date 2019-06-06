package com.example.admin.oracletest.ui.main.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

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
import retrofit2.Callback;
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

    public LiveData<GetRequestsResource<RequestsPage>> searchRequests(String sql_stm, String sql_stm_rows_count, int page_number){
        return LiveDataReactiveStreams.fromPublisher(requestsApi.searchRequests(sql_stm, sql_stm_rows_count, page_number)
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
                    User.search_requests_amount = requestsPage.getRequests_amount();
                    Log.d(TAG, "apply: success");
                    Log.d(TAG, "apply: " + requestsPage.getRequests_amount());
                    return GetRequestsResource.success(requestsPage);
                }
            }));
    }

}
