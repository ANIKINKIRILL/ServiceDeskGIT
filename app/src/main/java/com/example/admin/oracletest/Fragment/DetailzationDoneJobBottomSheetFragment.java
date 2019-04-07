package com.example.admin.oracletest.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.oracletest.R;
import com.example.admin.oracletest.Utils.DetailzationDoneJobBottomSheetFragmentRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Всплывающий фрагмент с выбором характеристики выполненных работ
 */

public class DetailzationDoneJobBottomSheetFragment extends BottomSheetDialogFragment{

    // Виджеты
    private RecyclerView recyclerView;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheetfragment_detailzation_done_job, container, false);
        init(view);
        populateRecyclerView();
        return view;
    }

    /**
     * Инициализация виджетов
     * @param view      окно фрагмента
     */

    private void init(View view){
        recyclerView = view.findViewById(R.id.recyclerView);
    }

    /**
     * Наполнить RecyclerView
     */

    private void populateRecyclerView(){
        DetailzationDoneJobBottomSheetFragmentRecyclerViewAdapter adapter =
                new DetailzationDoneJobBottomSheetFragmentRecyclerViewAdapter(detailzationChoices, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

}
