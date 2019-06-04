package com.example.admin.oracletest.dependencyinjection.main;

import com.example.admin.oracletest.network.main.RequestsApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class MainModule {

    @MainScope
    @Provides
    static RequestsApi provideRequestsApi(Retrofit retrofit){
        return retrofit.create(RequestsApi.class);
    }

}
