package com.example.admin.oracletest.dependencyinjection.main;

import android.arch.lifecycle.ViewModel;

import com.example.admin.oracletest.ui.main.all_requests.AllRequestsFragmentViewModel;
import com.example.admin.oracletest.ui.main.my_requests.MyRequestsFragmentViewModel;
import com.example.admin.oracletest.ui.main.settings.SettingsFragmentViewModel;
import com.example.admin.oracletest.viewmodel.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SettingsFragmentViewModel.class)
    abstract ViewModel bindSettingsViewModel(SettingsFragmentViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AllRequestsFragmentViewModel.class)
    abstract ViewModel bindAllRequestsViewModel(AllRequestsFragmentViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MyRequestsFragmentViewModel.class)
    abstract ViewModel bindMyRequestsFragmentViewModel(MyRequestsFragmentViewModel viewModel);

}
