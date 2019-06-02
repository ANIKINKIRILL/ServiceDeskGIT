package com.example.admin.oracletest.dependencyinjection.auth;

import android.arch.lifecycle.ViewModel;

import com.example.admin.oracletest.ViewModelKey;
import com.example.admin.oracletest.ui.auth.AuthViewModel;

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
