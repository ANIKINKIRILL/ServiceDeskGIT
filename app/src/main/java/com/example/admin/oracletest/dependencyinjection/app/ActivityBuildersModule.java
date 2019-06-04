package com.example.admin.oracletest.dependencyinjection.app;

import com.example.admin.oracletest.dependencyinjection.auth.AuthModule;
import com.example.admin.oracletest.dependencyinjection.auth.AuthScope;
import com.example.admin.oracletest.dependencyinjection.auth.AuthViewModelModule;
import com.example.admin.oracletest.dependencyinjection.main.MainFragmentsBuilderModule;
import com.example.admin.oracletest.dependencyinjection.main.MainScope;
import com.example.admin.oracletest.dependencyinjection.main.MainViewModelModule;
import com.example.admin.oracletest.ui.auth.AuthActivity;
import com.example.admin.oracletest.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
            modules = {
                    AuthViewModelModule.class,
                    AuthModule.class
            }
    )
    abstract AuthActivity contributeAuthActivity();

    @MainScope
    @ContributesAndroidInjector(
            modules = {
                    MainFragmentsBuilderModule.class,
                    MainViewModelModule.class
            }
    )
    abstract MainActivity contributeMainActivity();

}
