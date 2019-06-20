package com.test.admin.servicedesk.dependencyinjection.main;

import com.test.admin.servicedesk.ui.main.BottomSheetDialogFragmentFilterEmployeeRequests;
import com.test.admin.servicedesk.ui.main.all_requests.AllRequestsFragment;
import com.test.admin.servicedesk.ui.main.my_requests.MyRequestsFragment;
import com.test.admin.servicedesk.ui.main.search.SearchFragment;
import com.test.admin.servicedesk.ui.main.search.ViewSearchFragment;
import com.test.admin.servicedesk.ui.main.settings.SettingsFragment;

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
