package com.test.admin.servicedesk.dependencyinjection.app;

import com.test.admin.servicedesk.dependencyinjection.auth.AuthModule;
import com.test.admin.servicedesk.dependencyinjection.auth.AuthScope;
import com.test.admin.servicedesk.dependencyinjection.auth.AuthViewModelModule;
import com.test.admin.servicedesk.dependencyinjection.main.MainFragmentsBuilderModule;
import com.test.admin.servicedesk.dependencyinjection.main.MainModule;
import com.test.admin.servicedesk.dependencyinjection.main.MainScope;
import com.test.admin.servicedesk.dependencyinjection.main.MainViewModelModule;
import com.test.admin.servicedesk.ui.auth.AuthActivity;
import com.test.admin.servicedesk.ui.main.MainActivity;
import com.test.admin.servicedesk.ui.request_details.DetailRequestActivity;

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
                    MainViewModelModule.class,
                    MainModule.class
            }
    )
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(
            modules = {

            }
    )
    abstract DetailRequestActivity contributeDetailRequestActivity();

}
