package com.example.admin.oracletest.dependencyinjection.main;

import com.example.admin.oracletest.ui.main.settings.SettingsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentsBuilderModule {

    @ContributesAndroidInjector
    abstract SettingsFragment contributeSettingsFragment();

}
