package com.example.admin.oracletest.network.main;

import com.example.admin.oracletest.models.RequestsPage;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestsApi {

    /**
     * Get current requests in {@link com.example.admin.oracletest.ui.main.all_requests.AllRequestsFragment}
     */

    @GET("SERVICEDESK_MOBILE_TEST.GET_CURRENT_REQUESTS")
    Flowable<RequestsPage> getCurrentRequests(
            @Query("p_page_number") int page_number,
            @Query("p_status_id") int status_id
    );

}
