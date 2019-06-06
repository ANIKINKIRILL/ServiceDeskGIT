package com.example.admin.oracletest;


import android.content.Context;

import com.example.admin.oracletest.dependencyinjection.app.DaggerAppComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class BaseApplication extends DaggerApplication {

    private static Context context;

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BaseApplication.context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }

}
