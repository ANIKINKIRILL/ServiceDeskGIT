package com.example.admin.oracletest.dependencyinjection.app;

import android.arch.lifecycle.ViewModelProvider;

import com.example.admin.oracletest.ViewModelProviderFactory;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelProviderFactoryModule {

    @Singleton
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory providerFactory);

}
