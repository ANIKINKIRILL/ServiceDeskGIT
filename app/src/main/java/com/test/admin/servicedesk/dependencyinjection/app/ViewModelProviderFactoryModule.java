package com.test.admin.servicedesk.dependencyinjection.app;

import android.arch.lifecycle.ViewModelProvider;

import com.test.admin.servicedesk.viewmodel.ViewModelProviderFactory;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelProviderFactoryModule {

    @Singleton
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory providerFactory);

}
