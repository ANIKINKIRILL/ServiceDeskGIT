package com.example.admin.oracletest.ui.auth;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.oracletest.R;
import com.example.admin.oracletest.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Authentication users activity
 */

public class AuthActivity extends DaggerAppCompatActivity implements View.OnClickListener {

    // Injections
    @Inject
    ViewModelProviderFactory providerFactory;

    // UI
    private ProgressBar progressBar;
    private TextView forgotPassword;
    private Button loginButton;
    private EditText login, password;

    // Vars
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
        setContentView(R.layout.activity_auth);
        init();
    }

    /**
     * Init ui components
     */

    private void init(){
        progressBar = findViewById(R.id.progressBar);
        forgotPassword = findViewById(R.id.forgotPasswordButton);
        loginButton = findViewById(R.id.loginButton);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);

        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
    }

    /**
     * Init {@link AuthViewModel}
     */

    private void initViewModel(){
        viewModel = ViewModelProviders.of(this, providerFactory).get(AuthViewModel.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:{
                break;
            }
            case R.id.forgotPasswordButton:{
                break;
            }
        }
    }
}
