package com.test.admin.servicedesk.network.auth;

import com.test.admin.servicedesk.models.User;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AuthApi {

    @GET("PORTAL_PG_MOBILE.AUTHENTICATION")
    Flowable<User> authenticateUser(@Query("p_login") String login, @Query("p_pass") String password);

}
