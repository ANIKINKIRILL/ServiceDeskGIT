package com.example.admin.oracletest.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.Models.User;
import com.example.admin.oracletest.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Активити для вхдода в систему ServiceDesk
 */

public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "AuthActivity";

    // widgets
    private EditText loginEditText, passwordEditText;
    private Button loginButton, forgotPasswordButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        init();
    }

    /**
     * Находим елементы, устанавлеваем слушатели
     */
    private void init(){
        loginEditText = findViewById(R.id.login);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);

        loginButton.setOnClickListener(loginOnClickListener);
        forgotPasswordButton.setOnClickListener(forgotPasswordOnClickListener);

    }

    /**
     * Слушатель для авторизации
     */

    Button.OnClickListener loginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String p_login = loginEditText.getText().toString();
            String p_password = passwordEditText.getText().toString();
            final int hash_password = p_password.hashCode();
            User.authenticate(p_login, p_password, new Callback() {
                @Override
                public void execute(Object data) {
                    try {
                        JSONObject jsonObject = new JSONObject(data.toString());
                        String password = jsonObject.getString("password");
                        String p_password_hashcode = jsonObject.getString("p_password_hashcode");
                        if(password.equals(p_password_hashcode)){
                            //авторизация прошла успешно, переходим на главное меню
                        }else{
                            //авторизация прошла неуспешно
                        }
                        Log.d(TAG, "execute: " + hash_password);
                    } catch (JSONException e) {
                        Log.d(TAG, "JSONException: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    /**
     * Слушатель, если пользователь забыл пароль
     */

    Button.OnClickListener forgotPasswordOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AuthActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
        }
    };
}
