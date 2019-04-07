package com.example.admin.oracletest.Activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.admin.oracletest.Fragment.DetailzationDoneJobBottomSheetFragment;
import com.example.admin.oracletest.R;
import com.example.admin.oracletest.Utils.DetailzationDoneJobRecyclerViewAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Активити с Детализация выполненных работ, Работы выполнены, Прикрепить файл
 */

public class DetailzationDoneJobActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "DetailzationDoneJobAct";

    // Виджеты
    private RecyclerView recyclerView;
    private RadioButton full, partly;
    private RadioGroup radioGroup;
    private TextView pickDetailzationText;


    // Постоянные переменные
    public static final ArrayList<String> detailzationChoices = new ArrayList<>(
            Arrays.asList("Изменение кода программы",
                    "Регистрация/изменение прав доступа пользователя ИАС",
                    "Работы по заявке произведены",
                    "Заявка перенаправлена в УО УМУ",
                    "Написана документация",
                    "Обучение пользователей работе с ИАС",
                    "Настройка справочников ИАС",
                    "Сформирован статистический отчет",
                    "Разработан новый модуль ИАС",
                    "Произведена корректировка данных ИАС")
    );
    public static final ArrayList<String> userDetailzationChoices = new ArrayList<>(
            Arrays.asList("Произведена корректировка данных ИАС", "Разработан новый модуль ИАС")
    );

    // Переменные
    private String cod;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailzation_done_job_activity);
        getIntentExtras();
        init();
        initActionBar();
        populateRecyclerView();
    }

    /**
     * Инициализация виджетов
     */

    private void init(){
        recyclerView = findViewById(R.id.detailzation_done_job_recycler_view);
        full = findViewById(R.id.full);
        partly = findViewById(R.id.partly);
        radioGroup = findViewById(R.id.radioGroup);
        pickDetailzationText = findViewById(R.id.pickDetailzationText);

        pickDetailzationText.setOnClickListener(this);
        full.setOnClickListener(this);
        partly.setOnClickListener(this);

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

    /**
     * Получить данные по intentExtra
     */

    private void getIntentExtras(){
        Bundle bundle = getIntent().getExtras();
        cod = bundle.getString(getString(R.string.requestNumber));
    }

    /**
     * Наполнить RecyclerView
     */

    private void populateRecyclerView(){
        DetailzationDoneJobRecyclerViewAdapter adapter = new DetailzationDoneJobRecyclerViewAdapter(userDetailzationChoices, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pickDetailzationText:{
                // откыть снизу всплывающие окно
                // с выбором характеристики
                DetailzationDoneJobBottomSheetFragment bottomSheetFragment = new DetailzationDoneJobBottomSheetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), getString(R.string.open_dialog));
                break;
            }
        }
    }
}