package com.test.admin.servicedesk.dependencyinjection.main;

import android.arch.lifecycle.ViewModel;

import com.test.admin.servicedesk.ui.main.all_requests.AllRequestsFragmentViewModel;
import com.test.admin.servicedesk.ui.main.my_requests.MyRequestsFragmentViewModel;
import com.test.admin.servicedesk.ui.main.search.SearchFragmentViewModel;
import com.test.admin.servicedesk.ui.main.settings.SettingsFragmentViewModel;
import com.test.admin.servicedesk.viewmodel.ViewModelKey;

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

    @Binds
    @IntoMap
    @ViewModelKey(SearchFragmentViewModel.class)
    abstract ViewModel bindSearchFragmentViewModel(SearchFragmentViewModel viewModel);

}
