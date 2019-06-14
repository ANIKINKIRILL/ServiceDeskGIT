package com.example.admin.oracletest.ui.main.settings;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.admin.oracletest.R;
import com.example.admin.oracletest.dependencyinjection.app.SessionManager;
import com.example.admin.oracletest.viewmodel.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * This fragment with basic settings configuration
 * User can logout, make some changes about notifications and stuff...
 */

public class SettingsFragment extends DaggerFragment implements View.OnClickListener {

    private static final String TAG = "SettingsFragment";

    // Ui
    private Button exitButton;

    // Injections
    @Inject
    SessionManager sessionManager;
    @Inject
    ViewModelProviderFactory providerFactory;

    // Vars
    private SettingsFragmentViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settingsfragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        viewModel = ViewModelProviders.of(this, providerFactory).get(SettingsFragmentViewModel.class);
    }

    /**
     * Init ui components
     * @param view  fragment window
     */

    private void init(View view){
        exitButton = view.findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exitButton:{
                sessionManager.logout();
                break;
            }
        }
    }

}