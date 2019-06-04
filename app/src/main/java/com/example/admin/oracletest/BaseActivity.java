package com.example.admin.oracletest;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.admin.oracletest.dependencyinjection.app.SessionManager;
import com.example.admin.oracletest.models.User;
import com.example.admin.oracletest.ui.auth.AuthActivity;
import com.example.admin.oracletest.ui.auth.AuthResource;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity extends DaggerAppCompatActivity {

    @Inject
    SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscribeObserver();
    }

    private void subscribeObserver(){
        sessionManager.getAuthUser().observe(this, new Observer<AuthResource<User>>() {
            @Override
            public void onChanged(@Nullable AuthResource<User> userAuthResource) {
                assert userAuthResource != null;
                switch (userAuthResource.status){
                    case NOT_AUTHENTICATED:{
                        Intent intent = new Intent(BaseActivity.this, AuthActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }
            }
        });
    }

}
