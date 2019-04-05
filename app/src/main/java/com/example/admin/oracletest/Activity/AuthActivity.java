package com.example.admin.oracletest.Activity;

import android.app.ProgressDialog;
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

import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.Models.EmployeeRequest;
import com.example.admin.oracletest.Models.ServerKFU;
import com.example.admin.oracletest.Models.User;
import com.example.admin.oracletest.R;
import com.example.admin.oracletest.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Активити для вхдода в систему ServiceDesk
 */

public class AuthActivity extends AppCompatActivity {

    // Logs
    private static final String TAG = "AuthActivity";

    // widgets
    private EditText loginEditText, passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordButton;
    private ProgressDialog progressDialog;

    // vars
    private static final String USER_LOGIN = "user_login";
    private static final String USER_PASSWORD = "user_password";
    public static final String SETTINGS = "settings";

    private static boolean isAuthorized; // Авторизован ли пользователь
    private static String login;
    private static String password;
    private static int user_id;
    private static String firstname;
    private static String lastname;
    private static String middlename;

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
     * Инициализация
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
            User.authenticate(p_login, p_password, new Callback() {
                @Override
                public void execute(Object data) {
                    boolean successful = (boolean)data;
                    if(successful){
                        progressDialog.dismiss();
                        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        Toast.makeText(AuthActivity.this, "Вы вошли в свой профиль", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(AuthActivity.this, "Извините, неверный логин или пароль", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            */

            Observable<String> authObservable = Observable.fromCallable(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    Log.d(TAG, "call authObservable thread: " + Thread.currentThread().getName());
                    return ServerKFU.authenticateUserRxJava(p_login, p_password);
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            authObservable.subscribe(new Observer<String>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(String s) {
                    makeOutJson(s);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });


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

    private void makeOutJson(Object data){
        try {
            JSONObject jsonObject = new JSONObject(data.toString());
            boolean successful = jsonObject.getBoolean("successful");
            if(successful){
                progressDialog.dismiss();
                user_id = jsonObject.getInt("user_id");
                firstname = jsonObject.getString("firstname");
                lastname = jsonObject.getString("lastname");
                middlename = jsonObject.getString("middlename");
                isAuthorized = true;
                saveInformation();
                Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(AuthActivity.this, "Извините, неверный логин или пароль", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Сохранение логина пароля пользователя в настройках
     */

    private static void saveInformation(){
        Settings.setUserLogin(login);
        Settings.setUserPassword(password);
        Settings.setUserId(Integer.toString(user_id));
        Settings.setUserFirstName(firstname);
        Settings.setUserMiddleName(middlename);
        Settings.setUserLastName(lastname);
    }

}
