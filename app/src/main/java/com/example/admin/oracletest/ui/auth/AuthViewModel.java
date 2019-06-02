package com.example.admin.oracletest.ui.auth;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * ViewModel for {@link AuthActivity}
 * Here`s gonna be all business logic and server tasks
 */

public class AuthViewModel extends ViewModel {

    private static final String TAG = "AuthViewModel";

    @Inject
    public AuthViewModel(Retrofit retrofit){
        
    }

}
