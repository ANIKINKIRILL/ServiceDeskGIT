package com.example.admin.oracletest.Activity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.oracletest.Models.EmployeeRequest;
import com.example.admin.oracletest.R;
import com.example.admin.oracletest.Settings;

/**
 * Активити с детальной информацией о заявке на инженера
 */

public class DetailRequestActivity extends AppCompatActivity {

    // Виджеты
    private TextView requestCode, requestDateOfRegistration, requestDateOfRealization,
            requestZaavitel, zaavitelPost, zaavitelBuildingKfu, zaavitelBuildingKfuRoomNumber,
            zaavitelContact, zaavitelPhone, requestBody, employeeData, requestStatus;
    private Button delayRequestButton, completeRequestButton;

    // Переменные
    private String cod, date_of_reg, date_of_realization, zaavitel, post,
            building_kfu, roomNumber, contact, phone, body, employee_building_kfu, status;
    private String employee_firstname;
    private String employee_middlename;
    private String employee_lastname;
    private String all_request_content;

    // Постоянные переменные
    public static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailrequest2);
        init();
        getIntentExtras();
        initActionBar();
        getUserFIO();
        setIntentExtrasToWidgets();
    }

    /**
     * Инициализация виджетов
     */

    private void init() {
        requestCode = findViewById(R.id.requestCode);
        requestDateOfRegistration = findViewById(R.id.requestDateOfRegistration);
        requestStatus = findViewById(R.id.requestStatus);
        requestDateOfRealization = findViewById(R.id.requestDateOfRealization);
        requestZaavitel = findViewById(R.id.requestZaavitel);
        zaavitelPost = findViewById(R.id.zaavitelPost);
        zaavitelBuildingKfu = findViewById(R.id.zaavitelBuildingKfu);
        zaavitelBuildingKfuRoomNumber = findViewById(R.id.zaavitelBuildingKfuRoomNumber);
        zaavitelContact = findViewById(R.id.zaavitelContact);
        zaavitelPhone = findViewById(R.id.zaavitelPhone);
        requestBody = findViewById(R.id.requestBody);
        employeeData = findViewById(R.id.employeeData);
        completeRequestButton = findViewById(R.id.completeRequestButton);
        delayRequestButton = findViewById(R.id.delayRequestButton);

        completeRequestButton.setOnClickListener((view) -> {
            Intent intent = new Intent(this, DescriptionDoneJobActivity.class);
            intent.putExtra(getString(R.string.requestNumber), cod);
            startActivity(intent);
        });

        zaavitelPhone.setOnClickListener((v) -> {
            makePhoneCall();
        });
    }

    /**
     * Инициализация ActionBar
     */

    private void initActionBar(){
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Карточка заявки " + cod);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.kfuDefaultColor)));
        actionBar.setElevation(0);
    }

    /**
     * Сделать звонок по телефону
     */

    private void makePhoneCall(){
        if(ContextCompat.checkSelfPermission(DetailRequestActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DetailRequestActivity.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }else{
            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
            phoneIntent.setData(Uri.parse("tel:" + zaavitelPhone.getText().toString()));
            startActivity(phoneIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_request_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                break;
            }
            case R.id.copy_request:{
                copyRequestData();
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
        status = bundle.getString(getString(R.string.requestStatus));
    }

    /**
     * Установить данные из intentExtras в виджеты
     */

    private void setIntentExtrasToWidgets(){
        requestCode.setText(String.format("Номер заяки: %s", cod));
        requestDateOfRealization.setText(String.format("Дата регистрация: %s", date_of_realization+"\n"));
        requestDateOfRegistration.setText(String.format("Срок выполнения: %s", date_of_reg));
        requestStatus.setText(String.format("Статус: %s", status));
        requestZaavitel.setText(String.format("Заявитель: %s", zaavitel));
        zaavitelPost.setText(String.format("Должность: %s", post));
        zaavitelBuildingKfu.setText(String.format("Адрес: %s", building_kfu));
        zaavitelBuildingKfuRoomNumber.setText(String.format("Кабинет: %s", roomNumber));
        zaavitelContact.setText(String.format("Контакт: %s", contact));
        zaavitelPhone.setText(String.format("Телефон: %s", phone+"\n"));
        requestBody.setText(String.format("Текст заявки: %s", body+"\n"));
        employeeData.setText(String.format("Исполнитель: %s %s %s",
                employee_middlename,
                employee_firstname,
                employee_lastname)
        );
        if(status.equals(EmployeeRequest.NEW)){
            employeeData.setText("Исполнитель: отсутствует");
        }
        all_request_content =
                requestCode.getText().toString() + "\n" +
                requestDateOfRealization.getText().toString() + "\n" +
                requestDateOfRegistration.getText().toString() + "\n" +
                requestZaavitel.getText().toString() + "\n" +
                zaavitelPost.getText().toString() + "\n" +
                zaavitelBuildingKfu.getText().toString() + "\n" +
                zaavitelBuildingKfuRoomNumber.getText().toString() + "\n" +
                zaavitelContact.getText().toString() + "\n" +
                zaavitelPhone.getText().toString() + "\n" +
                requestBody.getText().toString() + "\n" +
                employeeData.getText().toString();
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

    /**
     * Копировать всю завку в буффер обмена
     */

    private void copyRequestData(){
        String copied_data = all_request_content;
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("employee_request_data", copied_data);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(this, "Заявка скопирована в буффер обмена", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makePhoneCall();
            }else{
                Toast.makeText(this, "Вы отменили использование телефона", Toast.LENGTH_LONG).show();
            }
        }
    }
}
