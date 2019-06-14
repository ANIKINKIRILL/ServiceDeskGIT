package com.example.admin.oracletest.dependencyinjection.app;

import android.app.Application;

import com.example.admin.oracletest.BaseApplication;
import com.example.admin.oracletest.dependencyinjection.main.MainScope;
import com.example.admin.oracletest.network.main.RequestsApi;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.Provides;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import retrofit2.Retrofit;

@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                ViewModelProviderFactoryModule.class,
                ActivityBuildersModule.class,
                AppModule.class
        }
)
public interface AppComponent extends AndroidInjector<BaseApplication> {

    SessionManager sessionManager();

    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

}
