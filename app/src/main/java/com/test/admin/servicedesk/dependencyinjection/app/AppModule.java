package com.test.admin.servicedesk.dependencyinjection.app;

import android.app.Application;

import com.test.admin.servicedesk.Constants;
import com.test.admin.servicedesk.NotificationHelper;
import com.test.admin.servicedesk.NotificationsService;
import com.test.admin.servicedesk.network.main.RequestsApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Singleton
    @Provides
    static Retrofit provideRetrofitInstance(Application application){
        Gson gson = new GsonBuilder().setLenient().create();
        return new Retrofit.Builder()
                .baseUrl(Constants.SERVER_IP)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    static RequestsApi provideRequestsApi(Retrofit retrofit){
        return retrofit.create(RequestsApi.class);
    }
}
