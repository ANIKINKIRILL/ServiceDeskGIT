package com.example.admin.oracletest.Fragment;

import android.app.AlertDialog;
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

import com.example.admin.oracletest.R;

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
                sql_statement += " AND DECLARANT_FIO = LIKE '%" + getTextFromEditText(zaavitel) + "%'";
            }else {
                sql_statement += "WHERE DECLARANT_FIO = LIKE '%" + getTextFromEditText(zaavitel) + "%'";
            }
        }

        if(!getTextFromEditText(roomNumber).isEmpty()){
            if(sql_statement.contains("WHERE")) {
                sql_statement += " AND ROOM_NUMBER = " + getTextFromEditText(roomNumber);
            }else {
                sql_statement += "WHERE ROOM_NUMBER = " + getTextFromEditText(roomNumber);
            }
        }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_button:{
                // Если хотя бы 1 ввод есть
                if(isValid()){
                    makeSqlStatement();
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
