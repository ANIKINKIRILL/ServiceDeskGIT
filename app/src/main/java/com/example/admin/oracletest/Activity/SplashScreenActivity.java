package com.example.admin.oracletest.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
    Активити при запуске приложения
 */

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Мы не устанавливаем xml при создании активити, а делаем это заранее в styles
        // Переходим на активити входа в систему, при этом убераем SplashActivity со стека
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}
