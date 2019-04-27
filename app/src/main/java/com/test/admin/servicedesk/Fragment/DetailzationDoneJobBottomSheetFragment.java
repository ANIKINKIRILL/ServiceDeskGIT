package com.test.admin.servicedesk.Fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.test.admin.servicedesk.Activity.DetailzationDoneJobActivity;
import com.test.admin.servicedesk.R;
import com.test.admin.servicedesk.Utils.DetailzationDoneJobBottomSheetFragmentRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Всплывающий фрагмент с выбором характеристики выполненных работ
 */

@SuppressLint("ValidFragment")
public class DetailzationDoneJobBottomSheetFragment extends BottomSheetDialogFragment{

    private static final String TAG = "BottomSheetFragment";

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

    public ArrayList<String> userDetailzationChoices;

    @SuppressLint("ValidFragment")
    public DetailzationDoneJobBottomSheetFragment(ArrayList<String> userDetailzationChoices) {
        this.userDetailzationChoices = userDetailzationChoices;
    }

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
                new DetailzationDoneJobBottomSheetFragmentRecyclerViewAdapter(detailzationChoices, userDetailzationChoices, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        try {
            getActivity().recreate();
        }catch (Exception e){
            Toast.makeText(getContext(), "Данные не были обновлены попробуйте еще раз", Toast.LENGTH_SHORT).show();
        }
    }
}
