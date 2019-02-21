package com.example.admin.oracletest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.Models.User;
import com.example.admin.oracletest.R;


/**
 * Активити для вхдода в систему ServiceDesk
 */

public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "AuthActivity";

    // widgets
    private EditText loginEditText, passwordEditText;
    private Button loginButton, forgotPasswordButton;

    // vars
    private static final String USER_LOGIN = "user_login";
    private static final String USER_PASSWORD = "user_password";
    public static final String SETTINGS = "settings";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences settingsSP = getSharedPreferences(SETTINGS, MODE_PRIVATE);
        String userLogin = settingsSP.getString(USER_LOGIN, "");
        String userPassword = settingsSP.getString(USER_PASSWORD, "");
        loginEditText.setText(userLogin);
        passwordEditText.setText(userPassword);
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
            User.authenticate(p_login, p_password, new Callback() {
                @Override
                public void execute(Object data) {
                    boolean successful = (boolean)data;
                    if(successful){
                        Toast.makeText(AuthActivity.this, "Вы вошли в свой профиль", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(AuthActivity.this, "Извините, неверный логин или пароль", Toast.LENGTH_SHORT).show();
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
