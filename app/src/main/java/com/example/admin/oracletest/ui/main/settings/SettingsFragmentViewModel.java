package com.example.admin.oracletest.ui.main.settings;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import javax.inject.Inject;

public class SettingsFragmentViewModel extends ViewModel {

    private static final String TAG = "SettingsFragmentViewMod";

    @Inject
    public SettingsFragmentViewModel(){
        Log.d(TAG, "SettingsFragmentViewModel: viewmodel is working...");
    }

}
