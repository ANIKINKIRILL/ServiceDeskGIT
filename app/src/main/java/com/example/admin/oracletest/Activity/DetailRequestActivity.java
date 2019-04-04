package com.example.admin.oracletest.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.admin.oracletest.R;
import com.example.admin.oracletest.Settings;

/**
 * Активити с детальной информацией о заявке на инженера
 */

public class DetailRequestActivity extends AppCompatActivity {

    // Виджеты
    private TextView requestCode, requestDateOfRegistration, requestDateOfRealization,
            requestZaavitel, zaavitelPost, zaavitelBuildingKfu, zaavitelBuildingKfuRoomNumber,
            zaavitelContact, zaavitelPhone, requestBody, employeeData;

    // Переменные
    private String cod, date_of_reg, date_of_realization, zaavitel, post,
            building_kfu, roomNumber, contact, phone, body, employee_building_kfu;
    private String employee_firstname;
    private String employee_middlename;
    private String employee_lastname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailrequest);
        init();
        getIntentExtras();
        initActionBar();
        getUserFIO();
        setIntentExtrasToWidgets();
    }

    /**
     * Инициализация виджетов
     */

    private void init(){
        requestCode = findViewById(R.id.requestCode);
        requestDateOfRegistration = findViewById(R.id.requestDateOfRegistration);
        requestDateOfRealization = findViewById(R.id.requestDateOfRealization);
        requestZaavitel = findViewById(R.id.requestZaavitel);
        zaavitelPost = findViewById(R.id.zaavitelPost);
        zaavitelBuildingKfu = findViewById(R.id.zaavitelBuildingKfu);
        zaavitelBuildingKfuRoomNumber = findViewById(R.id.zaavitelBuildingKfuRoomNumber);
        zaavitelContact = findViewById(R.id.zaavitelContact);
        zaavitelPhone = findViewById(R.id.zaavitelPhone);
        requestBody = findViewById(R.id.requestBody);
        employeeData = findViewById(R.id.employeeData);
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

    /**
     * Получить данные по intentExtra
     */

    private void getIntentExtras(){
        Bundle bundle = getIntent().getExtras();
        cod = bundle.getString(getString(R.string.requestNumber));
        date_of_realization = bundle.getString(getString(R.string.date_of_realization));
        date_of_reg = bundle.getString(getString(R.string.date_of_reg));
        zaavitel = bundle.getString(getString(R.string.zaavitel));
        post = bundle.getString(getString(R.string.post));
        building_kfu = bundle.getString(getString(R.string.building_kfu_name));
        roomNumber = bundle.getString(getString(R.string.roomNumber));
        contact = "";
        phone = bundle.getString(getString(R.string.phone));
        body = bundle.getString(getString(R.string.body));
        employee_building_kfu = "";
    }

    /**
     * Установить данные из intentExtras в виджеты
     */

    private void setIntentExtrasToWidgets(){
        requestCode.setText(String.format("Номер заяки: %s", cod));
        requestDateOfRealization.setText(String.format("Дата регистрация: %s", date_of_realization));
        requestDateOfRegistration.setText(String.format("Дата рекомендуемого выполнения: %s", date_of_reg));
        requestZaavitel.setText(String.format("Заявитель: %s", zaavitel));
        zaavitelPost.setText(String.format("Должность: %s", post));
        zaavitelBuildingKfu.setText(String.format("Подразделение: %s", building_kfu));
        zaavitelBuildingKfuRoomNumber.setText(String.format("Кабинет: %s", roomNumber));
        zaavitelContact.setText(String.format("Контакт: %s", contact));
        zaavitelPhone.setText(String.format("Телефон: %s", phone));
        requestBody.setText(String.format("Текст заявки: %s", body));
        employeeData.setText(String.format("Исполнитель: %s %s %s",
                employee_middlename,
                employee_firstname,
                employee_lastname)
        );
    }

    /**
     * Получить фио пользователя
     */

    private void getUserFIO(){
        SharedPreferences sharedPreferences = getSharedPreferences(Settings.SETTINGS, MODE_PRIVATE);
        employee_firstname = sharedPreferences.getString(Settings.USER_FIRST_NAME, "");
        employee_middlename = sharedPreferences.getString(Settings.USER_MIDDLE_NAME, "");
        employee_lastname = sharedPreferences.getString(Settings.USER_LAST_NAME, "");
    }

}
