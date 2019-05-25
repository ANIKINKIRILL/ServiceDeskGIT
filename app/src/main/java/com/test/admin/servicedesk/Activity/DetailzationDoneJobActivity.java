package com.test.admin.servicedesk.Activity;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.test.admin.servicedesk.Fragment.DetailzationDoneJobBottomSheetFragment;
import com.test.admin.servicedesk.R;
import com.test.admin.servicedesk.Utils.DetailzationDoneJobRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Активити с Детализация выполненных работ, Работы выполнены, Прикрепить файл
 */

public class DetailzationDoneJobActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "DetailzationDoneJobAct";

    // Виджеты
    private RadioButton full, partly;
    private RadioGroup radioGroup;
    private TextView pickDetailzationText;

    // Переменные
    private DetailzationDoneJobRecyclerViewAdapter adapter;


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
            /* Список характеристик, которые выбрал пользователь */
    );

    // Переменные
    private String cod;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: called");
        setContentView(R.layout.activity_detailzation_done_job_activity);
        getIntentExtras();
        init();
        initActionBar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: called");
        populateRecyclerView();
        removeUserDetailzationChoicesDuplicateElements();
        Log.d(TAG, "onStart: " + userDetailzationChoices.size());
    }

    /**
     * Инициализация виджетов
     */

    private void init(){
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

    public void populateRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.detailzation_done_job_recycler_view);
        adapter = new DetailzationDoneJobRecyclerViewAdapter(userDetailzationChoices, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    /**
     * Удалить дубликаты со списка характеристик, которые выбрал пользователь
     */

    private void removeUserDetailzationChoicesDuplicateElements(){
        Set<String> removedList = new HashSet<>(userDetailzationChoices);
        userDetailzationChoices.clear();
        userDetailzationChoices.addAll(removedList);
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
                DetailzationDoneJobBottomSheetFragment bottomSheetFragment = new DetailzationDoneJobBottomSheetFragment(userDetailzationChoices);
                bottomSheetFragment.show(getSupportFragmentManager(), getString(R.string.open_dialog));
                break;
            }
        }
    }
}
