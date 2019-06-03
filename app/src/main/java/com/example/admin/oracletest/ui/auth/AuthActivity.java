package com.example.admin.oracletest.ui.auth;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.admin.oracletest.R;
import com.example.admin.oracletest.models.User;
import com.example.admin.oracletest.viewmodel.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Authentication users activity
 */

public class AuthActivity extends DaggerAppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AuthActivity";

    // Injections
    @Inject
    ViewModelProviderFactory providerFactory;

    // UI
    private TextView forgotPassword;
    private Button loginButton;
    private EditText login, password;
    private ProgressDialog progressDialog;

    // Vars
    private AuthViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
        setContentView(R.layout.activity_auth);
        init();

        subscribeObserver();

    }

    /**
     * Init ui components
     */

    private void init(){
        forgotPassword = findViewById(R.id.forgotPasswordButton);
        loginButton = findViewById(R.id.loginButton);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Входим в Ваш аккаунт");

        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
    }

    /**
     * Init {@link AuthViewModel}
     */

    private void initViewModel(){
        viewModel = ViewModelProviders.of(this, providerFactory).get(AuthViewModel.class);
    }

    /**
     * Show or Hide progress dialog
     * @param isVisible  show or hide
     * if true -> show otherwise hide
     */

    private void showProgressDialog(boolean isVisible){
        if(isVisible){
            progressDialog.show();
        }else{
            progressDialog.dismiss();
        }
    }

    /**
     * Check validation fields
     */

    private boolean isValid(){
        if(TextUtils.isEmpty(login.getText().toString().trim())){
            YoYo.with(Techniques.Shake).duration(1000).repeat(0).playOn(login);
            login.setError("Поле обязательно");
            return false;
        }
        if(TextUtils.isEmpty(password.getText().toString().trim())){
            YoYo.with(Techniques.Shake).duration(1000).repeat(0).playOn(login);
            login.setError("Поле обязательно");
            return false;
        }
        return true;
    }

    private void subscribeObserver(){
        viewModel.observeAuthUser().observe(this, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(@Nullable AuthResource<User> userAuthResource) {
                if(userAuthResource != null){
                    Log.d(TAG, "onChanged: " + userAuthResource.status.name());
                    switch (userAuthResource.status){
                        case AUTHENTICATED:{
                            showProgressDialog(false);
                            if(userAuthResource.data.isSuccessful()){
                                Log.d(TAG, "onChanged: logged in");
                            }else{
                                YoYo.with(Techniques.Shake).duration(1000).repeat(0).playOn(loginButton);
                                Toast.makeText(AuthActivity.this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                        case ERROR:{
                            showProgressDialog(false);
                            break;
                        }
                        case LOADING:{
                            showProgressDialog(true);
                            break;
                        }
                        case NOT_AUTHENTICATED:{
                            showProgressDialog(false);
                            break;
                        }
                    }
                }
            }
        });
    }

    private void authenticateUser(){
        viewModel.authenticateUser(login.getText().toString().trim(), password.getText().toString().trim());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:{
                if(isValid()){
                    authenticateUser();
                }
                break;
            }
            case R.id.forgotPasswordButton:{
                break;
            }
        }
    }
}
