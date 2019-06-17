package com.example.admin.oracletest.network.main;

import com.example.admin.oracletest.models.KfuBuildingsLocationsPage;
import com.example.admin.oracletest.models.RequestsPage;
import com.example.admin.oracletest.models.TechGroupsPage;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestsApi {

    /**
     * Get current requests in {@link com.example.admin.oracletest.ui.main.all_requests.AllRequestsFragment}
     */

    @GET("SERVICEDESK_MOBILE_TEST.GET_CURRENT_REQUESTS")
    Call<RequestsPage> getCurrentRequests(
            @Query("p_page_number") int page_number,
            @Query("p_status_id") int status_id
    );

    /**
     * Get authenticated user requests in {@link com.example.admin.oracletest.ui.main.my_requests.MyRequestsFragment}
     */

    @GET("SERVICEDESK_MOBILE_TEST.GET_EMPLOYEE_REQUESTS")
    Call<RequestsPage> getMyRequests(
            @Query("p_emp_id") int emp_id,
            @Query("p_page_number") int page_number,
            @Query("p_status_id") int status_id
    );

    /**
     * Get searched requests in {@link com.example.admin.oracletest.ui.main.search.SearchFragment}
     */

    @GET("SERVICEDESK_MOBILE_TEST.SEARCH_REQUEST")
    Call<RequestsPage> searchRequests(
            @Query(value = "p_sql_statement", encoded = true) String sql_statement,
            @Query(value = "p_sql_statement_count_rows", encoded = true) String sql_statement_rows_count,
            @Query("p_page_number") int page_number
    );

    /**
     * Get building kfu location names for autocompletetextview in {@link com.example.admin.oracletest.ui.main.search.SearchFragment}
     */

    @GET("SERVICEDESK_MOBILE_TEST.GET_KFU_BUILDINGS_LOCATIONS")
    Call<KfuBuildingsLocationsPage> getKfuBuildingsLocationNames();

    /**
     * Get tech groups for spinner in {@link com.example.admin.oracletest.ui.main.search.SearchFragment}
     */

    @GET("SERVICEDESK_MOBILE_TEST.GET_TECH_GROUPS")
    Call<TechGroupsPage> getTechGroups();

}
