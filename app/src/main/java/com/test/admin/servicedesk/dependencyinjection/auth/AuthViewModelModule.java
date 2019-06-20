package com.test.admin.servicedesk.dependencyinjection.auth;

import android.arch.lifecycle.ViewModel;

import com.test.admin.servicedesk.viewmodel.ViewModelKey;
import com.test.admin.servicedesk.ui.auth.AuthViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AuthViewModelModule {

    @AuthScope
    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel.class)
    abstract ViewModel bindAuthViewModel(AuthViewModel viewModel);

}
