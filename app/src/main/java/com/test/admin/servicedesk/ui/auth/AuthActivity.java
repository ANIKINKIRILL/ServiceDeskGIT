package com.test.admin.servicedesk.ui.auth;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.test.admin.servicedesk.R;
import com.test.admin.servicedesk.models.User;
import com.test.admin.servicedesk.network.main.RequestsApi;
import com.test.admin.servicedesk.ui.forgetPassword.ForgetPasswordActivity;
import com.test.admin.servicedesk.ui.main.MainActivity;
import com.test.admin.servicedesk.viewmodel.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

import static com.test.admin.servicedesk.BaseApplication.getContext;

/**
 * Authentication users activity
 */

public class AuthActivity extends DaggerAppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AuthActivity";

    // Injections
    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    RequestsApi requestsApi;

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

    @Override
    protected void onStart() {
        super.onStart();
        login.setText(User.getUserLogin());
        password.setText(User.getUserPassword());
    }

    /**
     * Init ui components
     */

    private void init(){
        forgotPassword = findViewById(R.id.forgotPasswordButton);
        loginButton = findViewById(R.id.loginButton);
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);

//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Входим в Ваш аккаунт");

        ColorDrawable colorDrawable = new ColorDrawable(getContext().getResources().getColor(R.color.kfuDefaultColor));
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Входим в Ваш аккаунт");
        progressDialog.setProgressDrawable(colorDrawable);

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
                                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                // LOADING LOCATIONS FROM SERVER
                                getKfuBuildingsLocationNames();
                                // LOADING TECH GROUPS FROM SERVER
                                getTechGroups();
                            }else{
                                YoYo.with(Techniques.Shake).duration(1000).repeat(0).playOn(loginButton);
                                Toast.makeText(AuthActivity.this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                            }
                            break;
                        }
                        case ERROR:{
                            User.setUserLogin("");
                            User.setUserPassword("");
                            showProgressDialog(false);
//                            showServerErrorAlertDialog();
                            Toast.makeText(AuthActivity.this, "Произошла ошибка. Обратитесь в тех.поддержку", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case LOADING:{
                            showProgressDialog(true);
                            break;
                        }
                        case NOT_AUTHENTICATED:{
                            User.setUserLogin("");
                            User.setUserPassword("");
                            showProgressDialog(false);
                            break;
                        }
                    }
                }
            }
        });
    }

    /**
     * If error happened
     */

    private void showServerErrorAlertDialog(){
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialog.setMessage("Произошла ошибка. Обратитесь в тех.поддержку");
        alertDialog.setPositiveButton(getContext().getText(R.string.write_to_tech_support), (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    private void authenticateUser(){
        User.setUserLogin(login.getText().toString().trim());
        User.setUserPassword(password.getText().toString().trim());
        viewModel.authenticateUser(login.getText().toString().trim(), password.getText().toString().trim());
    }

    /**
     * Get kfu buildings location names
     */

    private void getKfuBuildingsLocationNames(){
        viewModel.getKfuBuildingsLocationNames();
    }

    /**
     * Get tech groups
     */

    private void getTechGroups(){
        viewModel.getTechGroups();
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
                Intent intent = new Intent(this, ForgetPasswordActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
