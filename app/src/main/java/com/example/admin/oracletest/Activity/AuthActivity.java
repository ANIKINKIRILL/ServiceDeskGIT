package com.example.admin.oracletest.Activity;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.Models.User;
import com.example.admin.oracletest.R;
import com.example.admin.oracletest.ViewModel.AuthActivityViewModel;


/**
 * Активити для вхдода в систему ServiceDesk
 */

public class AuthActivity extends AppCompatActivity {

    // Лог
    private static final String TAG = "AuthActivity";

    // UI Компоненты
    private EditText loginEditText, passwordEditText;
    private ProgressDialog progressDialog;

    // Переменные
    private static final String USER_LOGIN = "user_login";
    private static final String USER_PASSWORD = "user_password";
    public static final String SETTINGS = "settings";
    private AuthActivityViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        initViewModel();
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
     * Инициализация ui компонентов
     */
    private void init(){
        loginEditText = findViewById(R.id.login);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        TextView forgotPasswordButton = findViewById(R.id.forgotPasswordButton);

        loginButton.setOnClickListener(loginOnClickListener);
        forgotPasswordButton.setOnClickListener(forgotPasswordOnClickListener);
    }

    /**
     * Инициализация {@link AuthActivityViewModel}
     */

    private void initViewModel(){
        viewModel = ViewModelProviders.of(this).get(AuthActivityViewModel.class);
    }

    /**
     * Button.OnClickListener для авторизации пользователья
     */

    Button.OnClickListener loginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String p_login = loginEditText.getText().toString();
            String p_password = passwordEditText.getText().toString();
            progressDialog = new ProgressDialog(AuthActivity.this);
            progressDialog.setMessage("Заходим в Ваш профиль...");
            progressDialog.show();
            /*
                Просим ViewModel сделать запрос на Repository, где второй
                идет на Сервер и возвращает JSON. ViewModel вся основная бизнесс
                логика (парсинг JSON)
            */
            viewModel.authenticateUser(AuthActivity.this, p_login, p_password, authenticateUserCallback);
        }
    };

    /**
     * Callback, который вернется полсе получения результата с
     * {@link AuthActivityViewModel}
     * Если data = true, то пользователь вошел в систему,
     * иначе авторизация неуспешна
     */

    private Callback authenticateUserCallback = new Callback() {
        @Override
        public void execute(Object data) {
            boolean successful = (boolean) data;
            if(successful){
                progressDialog.dismiss();
                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }else{
                progressDialog.dismiss();
                YoYo.with(Techniques.Shake).repeat(0).duration(1000).playOn(loginEditText);
                YoYo.with(Techniques.Shake).repeat(0).duration(1000).playOn(passwordEditText);
                Toast.makeText(AuthActivity.this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * Button.OnClickListener, если пользователь забыл пароль
     */

    Button.OnClickListener forgotPasswordOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AuthActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
        }
    };
}
