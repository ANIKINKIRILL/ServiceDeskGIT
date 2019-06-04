package com.example.admin.oracletest.network.auth;

import com.example.admin.oracletest.models.User;
import com.example.admin.oracletest.ui.auth.AuthResource;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AuthApi {

    @GET("PORTAL_PG_MOBILE.AUTHENTICATION")
    Flowable<User> authenticateUser(@Query("p_login") String login, @Query("p_pass") String password);

}
