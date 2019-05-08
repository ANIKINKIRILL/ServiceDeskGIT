package com.example.admin.oracletest.Activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;

import com.example.admin.oracletest.R;

/**
 * Активити с описаннием выполенныех работ
 */

public class DescriptionDoneJobActivity extends AppCompatActivity {

    // Переменные
    private String cod;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_done_job_activity);
        getIntentExtras();
        init();
        initActionBar();
    }

    /**
     * Инициализация виджетов
     */

    private void init(){
        Button nextButton = findViewById(R.id.nextButton);

        nextButton.setOnClickListener((v) -> {
            Intent intent = new Intent(DescriptionDoneJobActivity.this, DetailzationDoneJobActivity.class);
            intent.putExtra(getString(R.string.requestNumber), cod);
            startActivity(intent);
        });
    }

    /**
     * Получить данные по intentExtra
     */

    private void getIntentExtras(){
        Bundle bundle = getIntent().getExtras();
        cod = bundle.getString(getString(R.string.requestNumber));
    }

    /**
     * Инициализация ActionBar
     */

    private void initActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Карточка заявки " + cod);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.kfuDefaultColor)));
        actionBar.setElevation(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
