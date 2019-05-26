package com.test.admin.servicedesk.Fragment.SearchRequestsFragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.test.admin.servicedesk.Activity.MainActivity;
import com.test.admin.servicedesk.Callback;
import com.test.admin.servicedesk.Models.EmployeeRequest;
import com.test.admin.servicedesk.Models.User;
import com.test.admin.servicedesk.R;
import com.test.admin.servicedesk.ViewModel.SearchRequestsFragmentViewModel;

import java.net.URLEncoder;
import java.util.ArrayList;

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
    private ProgressDialog progressDialog;

    // Переменные
    private String sql_statement = "SELECT * FROM TECH_CENTER$DB.REQUEST ";
    private String sql_statement_count_rows = "SELECT COUNT(ID) FROM TECH_CENTER$DB.REQUEST ";
    private SearchRequestsFragmentViewModel viewModel;
    private int START_PAGE = 1;
    public static OnViewSearchRequestsFragmentListener requestsFragmentListener;
    public static OnViewSearchRequestsFragmentSqlParams requestsFragmentSqlParams;
    private LiveData<ArrayList<EmployeeRequest>> requests;


    /*----------------------------------- LIFECYCLE --------------------------------------*/


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
        configActionBar();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_requests_fragment, container, false);
        init(view);
        return view;
    }

    /*------------------------------------ Class Methods --------------------------------------*/


    /**
     * Настройка ActionBar
     */

    private static void configActionBar(){
        ActionBar actionBar = MainActivity.actionBar;
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.custom_action_bar_search_requests_fragment);
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
     * Инициализация {@link SearchRequestsFragmentViewModel}
     */

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
     * @param editText      EditText с которого хотим получить текст
     * @return полученный текст с editText
     */

    private String getTextFromEditText(EditText editText){
        return editText.getText().toString().trim();
    }

    /**
     * Создать SQL запрос
     * для основного запроса 'sql_statement'
     * для запроса с поиском кол-ва найденных заявок 'sql_statement_count_rows'
     */

    private void makeSqlStatement(){
        sql_statement = "SELECT * FROM TECH_CENTER$DB.REQUEST ";
        sql_statement_count_rows = "SELECT COUNT(ID) FROM TECH_CENTER$DB.REQUEST ";
        if(!getTextFromEditText(code).isEmpty()){
            sql_statement += "WHERE COD = " + getTextFromEditText(code);
            sql_statement_count_rows += "WHERE COD = " + getTextFromEditText(code);
        }

        if(!getTextFromEditText(zaavitel).isEmpty()){
            try {
                if (sql_statement.contains("WHERE")) {
                    sql_statement += " AND INSTR(DECLARANT_FIO,'" + URLEncoder.encode(getTextFromEditText(zaavitel), "Cp1251") + "'" + ") > 0";
                } else {
                    sql_statement += "WHERE INSTR(DECLARANT_FIO,'" + URLEncoder.encode(getTextFromEditText(zaavitel), "Cp1251") + "'" + ") > 0";
                }

                if (sql_statement_count_rows.contains("WHERE")) {
                    sql_statement_count_rows += " AND INSTR(DECLARANT_FIO,'" + URLEncoder.encode(getTextFromEditText(zaavitel), "Cp1251") + "'" + ") > 0";
                } else {
                    sql_statement_count_rows += "WHERE INSTR(DECLARANT_FIO,'" + URLEncoder.encode(getTextFromEditText(zaavitel), "Cp1251") + "'" + ") > 0";
                }
            }catch (Exception e){
                Log.d(TAG, "makeSqlStatement: " + e.getMessage());
            }
        }

        if(!getTextFromEditText(roomNumber).isEmpty()){
            if(sql_statement.contains("WHERE")) {
                sql_statement += " AND ROOM_NUM = '" + getTextFromEditText(roomNumber) + "'";
            }else {
                sql_statement += "WHERE ROOM_NUM = '" + getTextFromEditText(roomNumber) + "'";
            }

            if(sql_statement_count_rows.contains("WHERE")) {
                sql_statement_count_rows += " AND ROOM_NUM = '" + getTextFromEditText(roomNumber) + "'";
            }else {
                sql_statement_count_rows += "WHERE ROOM_NUM = '" + getTextFromEditText(roomNumber) + "'";
            }

        }

        sql_statement = sql_statement + " ORDER BY (ID) DESC";

        Log.d(TAG, "makeSqlStatement: " + sql_statement); Log.d(TAG, "makeSqlStatement: " + sql_statement_count_rows);

    }

    /**
     * Отчистить все данные с виджетов
     */

    public static void clearAllWidgetsData(){
        try {
            code.getText().clear();
            zaavitel.getText().clear();
            roomNumber.getText().clear();
        }catch (Exception e){
            Log.d(TAG, "clearAllWidgetsData: " + e.getMessage());
        }
    }

    /**
     * Callback со списком найденных заявок
     */

    private Callback mResultCallback = new Callback() {
        @Override
        public void execute(Object data) {
            progressDialog.dismiss();
            requests = (LiveData<ArrayList<EmployeeRequest>>) data;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // Нажатие на кнопку 'Показать результаты'
            case R.id.search_button:{
                // Проверка если хотя бы 1 ввод есть
                if(isValid()){
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Поиск заявок...");
                    progressDialog.show();
                    makeSqlStatement();
                    viewModel.search_request(sql_statement, sql_statement_count_rows, START_PAGE, mResultCallback, getContext(), contactView);
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /**
         * Если класс {@link com.test.admin.servicedesk.Activity.MainActivity} (на котором нах-ся фрагменты) не implements интерфесы
         * {@link OnViewSearchRequestsFragmentSqlParams} и {@link OnViewSearchRequestsFragmentListener}
         */
        if(context instanceof OnViewSearchRequestsFragmentListener && context instanceof OnViewSearchRequestsFragmentSqlParams){
            requestsFragmentListener = (OnViewSearchRequestsFragmentListener) context;
            requestsFragmentSqlParams = (OnViewSearchRequestsFragmentSqlParams) context;
        }else{
            Toast.makeText(context, "Ошибка! Обратитесь с администратору", Toast.LENGTH_SHORT).show();
        }
    }

    /*------------------------ CONTACT --------------------------*/

    private SearchRequestsFragmentContact.View contactView = new SearchRequestsFragmentContact.View() {
        @Override
        public void userHasSomeRequests() {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle("Поиск");
            dialog.setMessage("Найденные заявки: " + User.search_requests_amount);
            dialog.setPositiveButton("ПОКАЗАТЬ", (dialog1, which) -> {
                // Закрытие диалогового окна
                dialog1.dismiss();
                // Замена фрагмента
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.main_container, new ViewSearchRequestsFragment())
                        .commit();
                // Передача списка с найденнами заявками ViewSearchRequestsFragment
                requestsFragmentListener.setRequests(requests.getValue());
                // Передача sql параметров на ViewSearchRequestsFragment
                requestsFragmentSqlParams.setSqlParams(sql_statement, sql_statement_count_rows);
            });
            dialog.show();
        }

        @Override
        public void userDoesNotHaveRequests() {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle("Поиск");
            dialog.setMessage("Найденные заявки: " + User.search_requests_amount);
            dialog.setPositiveButton("ОТМЕНА", (dialog1, which) -> dialog1.dismiss());
            dialog.show();
        }
    };


}
