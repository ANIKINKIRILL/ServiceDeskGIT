package com.example.admin.oracletest.ui.main.search;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.Constants;
import com.example.admin.oracletest.R;
import com.example.admin.oracletest.models.EmployeeRequest;
import com.example.admin.oracletest.models.KfuBuildingLocation;
import com.example.admin.oracletest.models.RequestsPage;
import com.example.admin.oracletest.models.User;
import com.example.admin.oracletest.viewmodel.ViewModelProviderFactory;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * Fragment with search functionality
 * User can search specific request
 * filter them by many parameters
 */

public class SearchFragment extends DaggerFragment implements View.OnClickListener {

    private static final String TAG = "SearchFragment";

    // Injections
    @Inject
    ViewModelProviderFactory providerFactory;

    // Ui
    private static EditText code, zaavitel, podrazdilenie, roomNumber, type, info;
    private static TextView reg_date, closing_date;
    private static Spinner otdel, emp_fio, status, reg_user;
    private static Button searchButton;
    private ProgressDialog progressDialog;
    private static AutoCompleteTextView location;

    // Vars
    private SearchFragmentViewModel viewModel;
    private String sql_statement = "SELECT * FROM TECH_CENTER$DB.REQUEST ";
    private String sql_statement_count_rows = "SELECT COUNT(ID) FROM TECH_CENTER$DB.REQUEST ";
    private int start_page = 1;
    public static OnViewSearchRequestsFragmentListener requestsFragmentListener;
    public static OnViewSearchRequestsFragmentSqlParams requestsFragmentSqlParams;
    public ArrayAdapter<String> adapter;
    private KfuBuildingLocation[] locations = Constants.locations;
    private String[] locationNames = new String[locations.length];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initViewModel();
        populateLocationNames();
        return inflater.inflate(R.layout.fragment_searchrequests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    /**
     * Init ui components
     * @param view      fragment window
     */

    private void init(View view){
        ColorDrawable colorDrawable = new ColorDrawable(getContext().getResources().getColor(R.color.kfuDefaultColor));
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Поиск заявок...");
        progressDialog.setProgressDrawable(colorDrawable);

        code = view.findViewById(R.id.requestCodeValue);
        reg_date = view.findViewById(R.id.requestDateOfRegistrationValue);
        closing_date = view.findViewById(R.id.requestDateOfClosingValue);
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
        reg_date.setOnClickListener(this);
        closing_date.setOnClickListener(this);
    }

    /**
     * Init {@link SearchFragmentViewModel}
     */

    private void initViewModel(){
        viewModel = ViewModelProviders.of(this, providerFactory).get(SearchFragmentViewModel.class);
    }

    /**
     * Put adres_name from each location object in locations array to
     * locationNames array
     */

    private void populateLocationNames(){
        for(int i = 0; i < locations.length; i++){
            locationNames[i] = locations[i].getAdres_name();
        }
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
        info.getText().toString().trim().isEmpty()
        &&
        reg_date.getText().toString().trim().isEmpty()
        &&
        closing_date.getText().toString().trim().isEmpty()){
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
     * Получить текст с TextView
     * @param textView      textView с которого хотим получить текст
     * @return полученный текст с editText
     */

    private String getTextFromTextView(TextView textView){
        return textView.getText().toString().trim();
    }

    private void showProgressDialog(boolean isVisible){
        if(isVisible){
            progressDialog.show();
        }else{
            progressDialog.dismiss();
        }
    }

    /**
     * Создать SQL запрос
     * для основного запроса 'sql_statement'
     * для запроса с поиском кол-ва найденных заявок 'sql_statement_count_rows'
     */

    private void makeSqlStatement(){
        sql_statement = "SELECT * FROM TECH_CENTER$DB.REQUEST ";
        sql_statement_count_rows = "SELECT COUNT(ID) FROM TECH_CENTER$DB.REQUEST ";

        // CODE

        if(!getTextFromEditText(code).isEmpty()){
            sql_statement += "WHERE COD = " + getTextFromEditText(code);
            sql_statement_count_rows += "WHERE COD = " + getTextFromEditText(code);
        }

        // DATE OF REGISTRATION

        if(!getTextFromTextView(reg_date).isEmpty()){
            if(sql_statement.contains("WHERE")) {
                sql_statement += " AND REQUEST_DATE LIKE TO_DATE('" + getTextFromTextView(reg_date) + "', 'DD/MM/YYYY')";
            }else {
                sql_statement += "WHERE REQUEST_DATE LIKE TO_DATE('" + getTextFromTextView(reg_date) + "', 'DD/MM/YYYY')";
            }

            if(sql_statement_count_rows.contains("WHERE")) {
                sql_statement_count_rows += " AND REQUEST_DATE LIKE TO_DATE('" + getTextFromTextView(reg_date) + "', 'DD/MM/YYYY')";
            }else {
                sql_statement_count_rows += "WHERE REQUEST_DATE LIKE TO_DATE('" + getTextFromTextView(reg_date) + "', 'DD/MM/YYYY')";
            }

        }

        // DATE OF CLOSING

        if(!getTextFromTextView(closing_date).isEmpty()){
            if(sql_statement.contains("WHERE")) {
                sql_statement += " AND DATE_OF_CLOSING LIKE TO_DATE('" + getTextFromTextView(closing_date) + "', 'DD/MM/YYYY')";
            }else {
                sql_statement += "WHERE DATE_OF_CLOSING LIKE TO_DATE('" + getTextFromTextView(closing_date) + "', 'DD/MM/YYYY')";
            }

            if(sql_statement_count_rows.contains("WHERE")) {
                sql_statement_count_rows += " AND DATE_OF_CLOSING LIKE TO_DATE('" + getTextFromTextView(closing_date) + "', 'DD/MM/YYYY')";
            }else {
                sql_statement_count_rows += "WHERE DATE_OF_CLOSING LIKE TO_DATE('" + getTextFromTextView(closing_date) + "', 'DD/MM/YYYY')";
            }

        }

        // DECLARANT_FIO

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

        // LOCATION

        if(!getTextFromEditText(location).isEmpty()){
            try {
                if (sql_statement.contains("WHERE")) {
                    sql_statement += " AND TECH_CENTER$DB.REQUEST.BUILDING_KFU_ID = (SELECT ID FROM TECH_CENTER$DB.BUILDING_KFU WHERE INSTR(TECH_CENTER$DB.BUILDING_KFU.ADRES_NAME, '" + URLEncoder.encode(getTextFromEditText(location), "Cp1251") +"') > 0 AND ROWNUM < 2)";
                } else {
                    sql_statement += "WHERE TECH_CENTER$DB.REQUEST.BUILDING_KFU_ID = (SELECT ID FROM TECH_CENTER$DB.BUILDING_KFU WHERE INSTR(TECH_CENTER$DB.BUILDING_KFU.ADRES_NAME, '" + URLEncoder.encode(getTextFromEditText(location), "Cp1251") +"') > 0 AND ROWNUM < 2)";
                }

                if (sql_statement_count_rows.contains("WHERE")) {
                    sql_statement_count_rows += " AND TECH_CENTER$DB.REQUEST.BUILDING_KFU_ID = (SELECT ID FROM TECH_CENTER$DB.BUILDING_KFU WHERE INSTR(TECH_CENTER$DB.BUILDING_KFU.ADRES_NAME, '" + URLEncoder.encode(getTextFromEditText(location), "Cp1251") +"') > 0 AND ROWNUM < 2)";
                } else {
                    sql_statement_count_rows += "WHERE TECH_CENTER$DB.REQUEST.BUILDING_KFU_ID = (SELECT ID FROM TECH_CENTER$DB.BUILDING_KFU WHERE INSTR(TECH_CENTER$DB.BUILDING_KFU.ADRES_NAME, '" + URLEncoder.encode(getTextFromEditText(location), "Cp1251") +"') > 0 AND ROWNUM < 2)";
                }
            }catch (Exception e){
                Log.d(TAG, "makeSqlStatement: " + e.getMessage());
            }
        }

        // ROOM_NUM

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

    private void searchRequests(){
        showProgressDialog(true);
        viewModel.searchRequests(sql_statement, sql_statement_count_rows, start_page, new Callback() {
            @Override
            public void result(Object data) {
                showProgressDialog(false);
                if(data != null){
                    RequestsPage requestsPage = (RequestsPage) data;
                    EmployeeRequest[] employeeRequests = requestsPage.getRequests();
                    if(employeeRequests.length > 0) {
                        List<EmployeeRequest> requests = new ArrayList<>(Arrays.asList(employeeRequests));
                        showSuccessAlertDialog(requests);
                    }else{
                        showErrorAlertDialog();
                    }
                }else{
                    showErrorAlertDialog();
                }
            }
        });
    }

    /**
     * Отчистить все данные с виджетов
     */

    public static void clearAllWidgetsData(){
        try {
            code.getText().clear();
            roomNumber.getText().clear();
            zaavitel.getText().clear();
            reg_date.setText("");
            closing_date.setText("");
            location.getText().clear();
        }catch (Exception e){
            Log.d(TAG, "clearAllWidgetsData: " + e.getMessage());
        }
    }

    /**
     * Success dialog. When user search is correct and result
     * can be shown
     */

    private void showSuccessAlertDialog(List<EmployeeRequest> requests){
        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(getContext());
        dialog.setTitle("Поиск");
        dialog.setMessage("Найденные заявки: " + User.search_requests_amount);
        dialog.setPositiveButton("ПОКАЗАТЬ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Закрытие диалогового окна
                dialog.dismiss();
                // Замена фрагмента
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.main_container, new ViewSearchFragment(), getString(R.string.view_search_fragment_tag))
                        .commit();
                // Передача списка с найденнами заявками ViewSearchRequestsFragment
                requestsFragmentListener.setRequests(requests);
                // Передача sql параметров на ViewSearchRequestsFragment
                requestsFragmentSqlParams.setSqlParams(sql_statement, sql_statement_count_rows);
            }
        });
        dialog.show();
    }

    /**
     * If user input is incorrect
     */

    private void showErrorInputAlertDialog(){
        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(getContext());
        dialog.setTitle("Неверный ввод");
        dialog.setMessage("Мы не сможем найти заявки без параметров. Введите какие-нибудь данные");
        dialog.setPositiveButton("OK", (dialogInterface, which) -> dialogInterface.dismiss());
        dialog.show();
    }

    /**
     * If search is failed
     * So we cannot find any requests
     */

    private void showErrorAlertDialog(){
        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(getContext());
        dialog.setTitle("Поиск");
        dialog.setMessage("Найденные заявки: " + User.search_requests_amount);
        dialog.setPositiveButton("ОТМЕНА", (dialog1, which) -> dialog1.dismiss());
        dialog.show();
    }

    /**
     * If user input is valid
     * do {@link User} search_request_amount value make zero value
     * makeSqlString and search in {@link SearchFragmentViewModel}
     */

    private void onSuccessSearchButton(){
        User.search_requests_amount = 0;
        makeSqlStatement();
        searchRequests();
    }

    /**
     * Set registration date
     * @param date  date to set
     */

    public static void setReg_date(String date){
        reg_date.setText(date);
    }

    /**
     * Set closing date
     * @param date  date to set
     */

    public static void setClosing_date(String date){
        closing_date.setText(date);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // Нажатие на кнопку 'Показать результаты'
            case R.id.search_button:{
                // Проверка если хотя бы 1 ввод есть
                if(isValid()){
                    onSuccessSearchButton();
                }else{
                    showErrorInputAlertDialog();
                }
                break;
            }
            case R.id.requestDateOfRegistrationValue:{
                BottomSheetDialogFragmentSearchRequestPickRegDate pickDateDialog = new BottomSheetDialogFragmentSearchRequestPickRegDate();
                pickDateDialog.show(getActivity().getSupportFragmentManager(), getString(R.string.open_dialog));
                break;
            }
            case R.id.requestDateOfClosingValue:{
                BottomSheetDialogFragmentSearchRequestPickDateOfClosing pickDateDialog = new BottomSheetDialogFragmentSearchRequestPickDateOfClosing();
                pickDateDialog.show(getActivity().getSupportFragmentManager(), getString(R.string.open_dialog));
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
}
