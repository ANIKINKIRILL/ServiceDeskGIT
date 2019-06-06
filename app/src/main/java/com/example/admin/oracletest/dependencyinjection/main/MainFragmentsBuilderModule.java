package com.example.admin.oracletest.dependencyinjection.main;

import com.example.admin.oracletest.ui.main.BottomSheetDialogFragmentFilterEmployeeRequests;
import com.example.admin.oracletest.ui.main.all_requests.AllRequestsFragment;
import com.example.admin.oracletest.ui.main.my_requests.MyRequestsFragment;
import com.example.admin.oracletest.ui.main.search.SearchFragment;
import com.example.admin.oracletest.ui.main.search.ViewSearchFragment;
import com.example.admin.oracletest.ui.main.settings.SettingsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentsBuilderModule {

    @ContributesAndroidInjector
    abstract SettingsFragment contributeSettingsFragment();

    @ContributesAndroidInjector
    abstract AllRequestsFragment contributeAllRequestsFragment();

    @ContributesAndroidInjector
    abstract BottomSheetDialogFragmentFilterEmployeeRequests contributeBottomSheetDialogFragmentFilterEmployeeRequests();

    @ContributesAndroidInjector
    abstract MyRequestsFragment contributeMyRequestsFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();

    @ContributesAndroidInjector
    abstract ViewSearchFragment contributeViewSearchFragment();

}
