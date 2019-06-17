package com.example.admin.oracletest.ui.auth;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.admin.oracletest.Constants;
import com.example.admin.oracletest.dependencyinjection.app.SessionManager;
import com.example.admin.oracletest.models.KfuBuildingLocation;
import com.example.admin.oracletest.models.KfuBuildingsLocationsPage;
import com.example.admin.oracletest.models.TechGroupsPage;
import com.example.admin.oracletest.models.User;
import com.example.admin.oracletest.network.auth.AuthApi;
import com.example.admin.oracletest.network.main.RequestsApi;

import javax.inject.Inject;

import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ViewModel for {@link AuthActivity}
 * Here`s gonna be all business logic and server tasks
 */

public class AuthViewModel extends ViewModel {

    private static final String TAG = "AuthViewModel";

    // Injections
    private AuthApi authApi;
    private RequestsApi requestsApi;
    private SessionManager sessionManager;

    @Inject
    public AuthViewModel(AuthApi authApi, RequestsApi requestsApi, SessionManager sessionManager){
        this.authApi = authApi;
        this.requestsApi = requestsApi;
        this.sessionManager = sessionManager;
    }

    public void getKfuBuildingsLocationNames(){
        Log.d(TAG, "getKfuBuildingsLocationNames: " + requestsApi.getKfuBuildingsLocationNames().request().url().toString());
        requestsApi.getKfuBuildingsLocationNames().enqueue(new Callback<KfuBuildingsLocationsPage>() {
            @Override
            public void onResponse(Call<KfuBuildingsLocationsPage> call, Response<KfuBuildingsLocationsPage> response) {
                Log.d(TAG, "onResponse: called");
                Constants.locations = response.body().getLocations();
            }

            @Override
            public void onFailure(Call<KfuBuildingsLocationsPage> call, Throwable t) {
                Log.d(TAG, "onFailure: called");
                Constants.locations = new KfuBuildingLocation[0];
            }
        });
    }

    public void getTechGroups(){
        Log.d(TAG, "getTechGroups: " + requestsApi.getTechGroups().request().url().toString());
        requestsApi.getTechGroups().enqueue(new Callback<TechGroupsPage>() {
            @Override
            public void onResponse(Call<TechGroupsPage> call, Response<TechGroupsPage> response) {
                Log.d(TAG, "onResponse: called");
            }

            @Override
            public void onFailure(Call<TechGroupsPage> call, Throwable t) {
                Log.d(TAG, "onFailure: called");
            }
        });
    }

    public void authenticateUser(String login, String password){
        LiveData<AuthResource<User>> source = LiveDataReactiveStreams.fromPublisher(
            authApi.authenticateUser(login, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, User>() {
                    @Override
                    public User apply(Throwable throwable) throws Exception {
                        User errorUser = new User();
                        errorUser.setUserId(-1);
                        return errorUser;
                    }
                })
                .map(new Function<User, AuthResource<User>>() {
                    @Override
                    public AuthResource<User> apply(User user) throws Exception {
                        if (user.getUserId() == -1) {
                            return AuthResource.error(Constants.API_ERROR_WHILE_LOADING, user);
                        }
                        return AuthResource.authenticated(user);
                    }
                })
        );
        sessionManager.authenticateUser(source);
    }

    public LiveData<AuthResource<User>> observeAuthUser(){
        return sessionManager.getAuthUser();
    }

}
