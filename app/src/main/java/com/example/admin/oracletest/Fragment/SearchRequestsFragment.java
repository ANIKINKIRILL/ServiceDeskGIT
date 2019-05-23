package com.example.admin.oracletest.Fragment;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.R;
import com.example.admin.oracletest.ViewModel.SearchRequestsFragmentViewModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *  Фрагмент с глобальным поиском по заявкам
 */

public class SearchRequestsFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "SearchRequestsFragment";

    // Виджеты
    private static EditText code, zaavitel, podrazdilenie,
    location, roomNumber, type, info;
    private static TextView reg_date, closing_date;
    private static Spinner otdel, emp_fio, status, reg_user;
    private static Button searchButton;

    // Переменные
    private String sql_statement = "SELECT * FROM TECH_CENTER$DB.REQUEST ";
    private SearchRequestsFragmentViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_requests_fragment, container, false);
        init(view);
        return view;
    }

    /**
     * Инициализация виджетов
     * @param view      окно фрагмента
     */

    private void init(View view){
        code = view.findViewById(R.id.requestCodeValue);
        reg_date = view.findViewById(R.id.requestDateOfRegistrationValue);
        closing_date = view.findViewById(R.id.requestDateOfRealizationValue);
        zaavitel = view.findViewById(R.id.requestZaavitelValue);
        podrazdilenie = view.findViewById(R.id.requestPodrazdelenieValue);
        location = view.findViewById(R.id.requestLocationValue);
        roomNumber = view.findViewById(R.id.requestRoomNumberValue);
        otdel = view.findViewById(R.id.requestOtdelValue);
        emp_fio = view.findViewById(R.id.requestEmployeeFioValue);
        status = view.findViewById(R.id.requestStatusValue);
        reg_user = view.findViewById(R.id.requestRegUserValue);
        type = view.findViewById(R.id.requestTypeValue);
        info = view.findViewById(R.id.requestInfoValue);
        searchButton = view.findViewById(R.id.search_button);

        searchButton.setOnClickListener(this);

    }

    private void initViewModel(){
        viewModel = ViewModelProviders.of(this).get(SearchRequestsFragmentViewModel.class);
    }

    /**
     * Проверка на правильность ввода
     * @return если хотя бы 1 ввод есть -> true
     *         если не 1-го ввода нет -> false
     */

    private boolean isValid(){
        if(code.getText().toString().trim().isEmpty()
        &&
        zaavitel.getText().toString().trim().isEmpty()
        &&
        podrazdilenie.getText().toString().trim().isEmpty()
        &&
        location.getText().toString().trim().isEmpty()
        &&
        roomNumber.getText().toString().trim().isEmpty()
        &&
        type.getText().toString().trim().isEmpty()
        &&
        info.getText().toString().trim().isEmpty()){
            return false;
        }
        return true;
    }

    /**
     * Получить текст с EditText
     * @return текст
     */

    private String getTextFromEditText(EditText editText){
        return editText.getText().toString().trim();
    }

    /**
     * Создать SQL запрос
     */

    private void makeSqlStatement(){
        sql_statement = "SELECT * FROM TECH_CENTER$DB.REQUEST ";
        if(!getTextFromEditText(code).isEmpty()){
            sql_statement += "WHERE COD = " + getTextFromEditText(code);
        }

        if(!getTextFromEditText(zaavitel).isEmpty()){
            if(sql_statement.contains("WHERE")) {
                try {
                    sql_statement += " AND INSTR(DECLARANT_FIO,'" + URLEncoder.encode(getTextFromEditText(zaavitel), "Cp1251") + "'" + ") > 0";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }else {
                sql_statement += "WHERE INSTR(DECLARANT_FIO,'" + getTextFromEditText(zaavitel) + "'" + ") > 0";
            }
        }

        if(!getTextFromEditText(roomNumber).isEmpty()){
            if(sql_statement.contains("WHERE")) {
                sql_statement += " AND ROOM_NUMBER = " + getTextFromEditText(roomNumber);
            }else {
                sql_statement += "WHERE ROOM_NUMBER = " + getTextFromEditText(roomNumber);
            }
        }

        //sql_statement = "'" + sql_statement + "'";

        Log.d(TAG, "makeSqlStatement: " + sql_statement);

    }

    /**
     * Отчистить все данные с виджетов
     */

    public static void clearAllWidgetsData(){
        code.getText().clear();
        zaavitel.getText().clear();
        roomNumber.getText().clear();
    }

    /**
     * Callback со списком найденных заявок
     */

    private Callback mResultCallback = new Callback() {
        @Override
        public void execute(Object data) {
            Log.d(TAG, "execute: " + data.toString());
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_button:{
                // Если хотя бы 1 ввод есть
                if(isValid()){
                    makeSqlStatement();
                    viewModel.search_request(sql_statement, mResultCallback, getContext());
                }else{
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("Неверный ввод");
                    dialog.setMessage("Мы не сможем найти заявки без параметров. Введите какие-нибудь данные");
                    dialog.setPositiveButton("OK", (dialogInterface, which) -> dialogInterface.dismiss());
                    dialog.show();
                }
                break;
            }
        }
    }



}
