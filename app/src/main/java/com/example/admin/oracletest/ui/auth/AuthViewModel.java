package com.example.admin.oracletest.ui.auth;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import javax.inject.Inject;

/**
 * ViewModel for {@link AuthActivity}
 * Here`s gonna be all business logic and server tasks
 */

public class AuthViewModel extends ViewModel {

    private static final String TAG = "AuthViewModel";

    @Inject
    public AuthViewModel(){
        Log.d(TAG, "AuthViewModel: viewmodel is working...");
    }

}
