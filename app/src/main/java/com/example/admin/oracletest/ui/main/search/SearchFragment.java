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
import com.example.admin.oracletest.models.TechGroup;
import com.example.admin.oracletest.models.User;
import com.example.admin.oracletest.utils.TechGroupsAdapter;
import com.example.admin.oracletest.viewmodel.ViewModelProviderFactory;

import java.io.UnsupportedEncodingException;
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
    private static TextView reg_date_from, reg_date_to, closing_date_from, closing_date_to;
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
    public ArrayAdapter<String> locationsNamesAdapter;
    public ArrayAdapter<String> statusAdapter;
    public TechGroupsAdapter techGroupsAdapter;
    private KfuBuildingLocation[] locations = Constants.locations;
    private String[] locationNames = new String[locations.length];
    private TechGroup[] techGroups = Constants.techGroups;

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

        locationsNamesAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, locationNames);

        statusAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, getActivity().getResources().getStringArray(R.array.requestStatusArrayWithEmptySelection));
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayList<TechGroup> techGroupArrayList = new ArrayList<>(Arrays.asList(techGroups));
        techGroupArrayList.add(0, new TechGroup(0,"",0,0,0, ""));
        techGroupsAdapter = new TechGroupsAdapter(view.getContext(), android.R.layout.simple_list_item_1, techGroupArrayList);

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
        reg_date_from = view.findViewById(R.id.requestDateOfRegistrationValueFrom);
        reg_date_to = view.findViewById(R.id.requestDateOfRegistrationValueTo);
        closing_date_from = view.findViewById(R.id.requestDateOfClosingValueFrom);
        closing_date_to = view.findViewById(R.id.requestDateOfClosingValueTo);
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
        reg_date_from.setOnClickListener(this);
        reg_date_to.setOnClickListener(this);
        closing_date_from.setOnClickListener(this);
        closing_date_to.setOnClickListener(this);

        location.setAdapter(locationsNamesAdapter);

        otdel.setAdapter(techGroupsAdapter);

        status.setAdapter(statusAdapter);

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
        TechGroup selectedTechGroup = (TechGroup) otdel.getSelectedItem();
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
        selectedTechGroup.getName().trim().isEmpty()
        &&
        type.getText().toString().trim().isEmpty()
        &&
        info.getText().toString().trim().isEmpty()
        &&
        (reg_date_from.getText().toString().trim().isEmpty() || reg_date_to.getText().toString().isEmpty())
        &&
        (closing_date_from.getText().toString().trim().isEmpty() || closing_date_to.getText().toString().isEmpty())
        &&
        status.getSelectedItem().toString().trim().isEmpty()){
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

    /**
     * Получить текст с Spinner
     * @param spinner      spinner с которого хотим получить текст
     * @return полученный текст с spinner
     */

    private String getTextFromSpinner(Spinner spinner){
        return spinner.getSelectedItem().toString().trim().toLowerCase();
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

        if(!getTextFromTextView(reg_date_from).isEmpty() && !getTextFromTextView(reg_date_to).isEmpty()){
            if(sql_statement.contains("WHERE")) {
                sql_statement += " AND REQUEST_DATE BETWEEN TO_DATE('" + getTextFromTextView(reg_date_from) + "', 'DD/MM/YYYY') AND TO_DATE('" + getTextFromTextView(reg_date_to) + "', 'DD/MM/YYYY')";
            }else {
                sql_statement += "WHERE REQUEST_DATE BETWEEN TO_DATE('" + getTextFromTextView(reg_date_from) + "', 'DD/MM/YYYY') AND TO_DATE('" + getTextFromTextView(reg_date_to) + "', 'DD/MM/YYYY')";
            }

            if(sql_statement_count_rows.contains("WHERE")) {
                sql_statement_count_rows += " AND REQUEST_DATE BETWEEN TO_DATE('" + getTextFromTextView(reg_date_from) + "', 'DD/MM/YYYY') AND TO_DATE('" + getTextFromTextView(reg_date_to) + "', 'DD/MM/YYYY')";
            }else {
                sql_statement_count_rows += "WHERE REQUEST_DATE BETWEEN TO_DATE('" + getTextFromTextView(reg_date_from) + "', 'DD/MM/YYYY') AND TO_DATE('" + getTextFromTextView(reg_date_to) + "', 'DD/MM/YYYY')";
            }

        }

        // DATE OF CLOSING

        if(!getTextFromTextView(closing_date_from).isEmpty() && !getTextFromTextView(closing_date_to).isEmpty()){
            if(sql_statement.contains("WHERE")) {
                sql_statement += " AND DATE_OF_CLOSING BETWEEN TO_DATE('" + getTextFromTextView(closing_date_from) + "', 'DD/MM/YYYY') AND TO_DATE('" + getTextFromTextView(closing_date_to) + "', 'DD/MM/YYYY')";
            }else {
                sql_statement += "WHERE DATE_OF_CLOSING BETWEEN TO_DATE('" + getTextFromTextView(closing_date_from) + "', 'DD/MM/YYYY') AND TO_DATE('" + getTextFromTextView(closing_date_to) + "', 'DD/MM/YYYY')";
            }

            if(sql_statement_count_rows.contains("WHERE")) {
                sql_statement_count_rows += " AND DATE_OF_CLOSING BETWEEN TO_DATE('" + getTextFromTextView(closing_date_from) + "', 'DD/MM/YYYY') AND TO_DATE('" + getTextFromTextView(closing_date_to) + "', 'DD/MM/YYYY')";
            }else {
                sql_statement_count_rows += "WHERE DATE_OF_CLOSING BETWEEN TO_DATE('" + getTextFromTextView(closing_date_from) + "', 'DD/MM/YYYY') AND TO_DATE('" + getTextFromTextView(closing_date_to) + "', 'DD/MM/YYYY')";
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

        // TECH_GROUP

        if(otdel.getSelectedItemPosition() != 0){
            TechGroup selectedTechGroup = (TechGroup) otdel.getSelectedItem();
            Log.d(TAG, "selectedTechGroup: " + selectedTechGroup.getCod());

            if(sql_statement.contains("WHERE")) {
                sql_statement += " AND ID IN (SELECT REQUEST_ID FROM TECH_CENTER$DB.REQUEST_WORKS WHERE TECH_GROUP_ID = " + selectedTechGroup.getCod() + ")";
            }else {
                sql_statement += "WHERE ID IN (SELECT REQUEST_ID FROM TECH_CENTER$DB.REQUEST_WORKS WHERE TECH_GROUP_ID = " + selectedTechGroup.getCod() + ")";
            }

            if(sql_statement_count_rows.contains("WHERE")) {
                sql_statement_count_rows += " AND ID IN (SELECT REQUEST_ID FROM TECH_CENTER$DB.REQUEST_WORKS WHERE TECH_GROUP_ID = " + selectedTechGroup.getCod() + ")";
            }else {
                sql_statement_count_rows += "WHERE ID IN (SELECT REQUEST_ID FROM TECH_CENTER$DB.REQUEST_WORKS WHERE TECH_GROUP_ID = " + selectedTechGroup.getCod() + ")";
            }
        }

        // STATUS

        if(!getTextFromSpinner(status).isEmpty()){
            int status_id = getStatusId(getTextFromSpinner(status));
            if(sql_statement.contains("WHERE")) {
                sql_statement += " AND STATUS_ID = " + status_id;
            }else {
                sql_statement += "WHERE STATUS_ID = " + status_id;
            }

            if(sql_statement_count_rows.contains("WHERE")) {
                sql_statement_count_rows += " AND STATUS_ID = " + status_id;
            }else {
                sql_statement_count_rows += "WHERE STATUS_ID = " + status_id;
            }
        }

        // REQUEST INFO

        if(!getTextFromEditText(info).isEmpty()){
            try {
                if(sql_statement.contains("WHERE")) {

                        sql_statement += " AND TECH_CENTER$DB.REQUEST.ID IN (SELECT REQUEST_ID FROM TECH_CENTER$DB.REQUEST_WORKS WHERE INSTR(TECH_CENTER$DB.REQUEST_WORKS.INFO, '" + URLEncoder.encode(getTextFromEditText(info), "Cp1251") + "') > 0 OR INSTR(TECH_CENTER$DB.REQUEST_WORKS.INFO, '" + URLEncoder.encode(getTextFromEditText(info).toLowerCase(), "Cp1251") + "') > 0)";
                }else {
                    sql_statement += "WHERE TECH_CENTER$DB.REQUEST.ID IN (SELECT REQUEST_ID FROM TECH_CENTER$DB.REQUEST_WORKS WHERE INSTR(TECH_CENTER$DB.REQUEST_WORKS.INFO, '" + URLEncoder.encode(getTextFromEditText(info), "Cp1251") + "') > 0 OR INSTR(TECH_CENTER$DB.REQUEST_WORKS.INFO, '" + URLEncoder.encode(getTextFromEditText(info).toLowerCase(), "Cp1251") + "') > 0)";
                }

                if(sql_statement_count_rows.contains("WHERE")) {
                    sql_statement_count_rows += " AND TECH_CENTER$DB.REQUEST.ID IN (SELECT REQUEST_ID FROM TECH_CENTER$DB.REQUEST_WORKS WHERE INSTR(TECH_CENTER$DB.REQUEST_WORKS.INFO, '" + URLEncoder.encode(getTextFromEditText(info), "Cp1251") + "') > 0 OR INSTR(TECH_CENTER$DB.REQUEST_WORKS.INFO, '" + URLEncoder.encode(getTextFromEditText(info).toLowerCase(), "Cp1251") + "') > 0)";
                }else {
                    sql_statement_count_rows += "WHERE TECH_CENTER$DB.REQUEST.ID IN (SELECT REQUEST_ID FROM TECH_CENTER$DB.REQUEST_WORKS WHERE INSTR(TECH_CENTER$DB.REQUEST_WORKS.INFO, '" + URLEncoder.encode(getTextFromEditText(info), "Cp1251") + "') > 0 OR INSTR(TECH_CENTER$DB.REQUEST_WORKS.INFO, '" + URLEncoder.encode(getTextFromEditText(info).toLowerCase(), "Cp1251") + "') > 0)";
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
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
            reg_date_from.setText("");
            reg_date_to.setText("");
            closing_date_from.setText("");
            closing_date_to.setText("");
            location.getText().clear();
            otdel.setSelection(0);
            status.setSelection(0);
            info.getText().clear();
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
        }).setNegativeButton("ОТМЕНА", (dialog1, which) -> dialog1.dismiss());
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
     * Set registration from date
     * @param date  date to set
     */

    public static void setReg_dateFrom(String date){
        reg_date_from.setText(date);
    }

    /**
     * Set registration to date
     * @param date  date to set
     */

    public static void setReg_dateTo(String date){
        reg_date_to.setText(date);
    }

    /**
     * Set closing from date
     * @param date  date to set
     */

    public static void setClosing_dateFrom(String date){
        closing_date_from.setText(date);
    }

    /**
     * Set closing to date
     * @param date  date to set
     */

    public static void setClosing_dateTo(String date){
        closing_date_to.setText(date);
    }

    /**
     * Получить id статуса заявки
     *
     * @param statusName название статуса
     * @return id статуса
     */

    private int getStatusId(String statusName) {
        int status_id = 1;
        switch (statusName) {
            case EmployeeRequest.NEW: {
                status_id = 1;
                break;
            }
            case EmployeeRequest.DONE: {
                status_id = 2;
                break;
            }
            case EmployeeRequest.IN_PROGRESS: {
                status_id = 3;
                break;
            }
            case EmployeeRequest.CANCELED: {
                status_id = 4;
                break;
            }
            case EmployeeRequest.STOPPED: {
                status_id = 5;
                break;
            }
            case EmployeeRequest.CLOSED: {
                status_id = 6;
                break;
            }
        }
        return status_id;
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
            case R.id.requestDateOfRegistrationValueFrom:{
                BottomSheetDialogFragmentSearchRequestPickRegDateFrom pickDateDialog = new BottomSheetDialogFragmentSearchRequestPickRegDateFrom();
                pickDateDialog.show(getActivity().getSupportFragmentManager(), getString(R.string.open_dialog));
                break;
            }
            case R.id.requestDateOfRegistrationValueTo:{
                BottomSheetDialogFragmentSearchRequestPickRegDateTo pickDateDialog = new BottomSheetDialogFragmentSearchRequestPickRegDateTo();
                pickDateDialog.show(getActivity().getSupportFragmentManager(), getString(R.string.open_dialog));
                break;
            }
            case R.id.requestDateOfClosingValueFrom:{
                BottomSheetDialogFragmentSearchRequestPickDateOfClosingFrom pickDateDialog = new BottomSheetDialogFragmentSearchRequestPickDateOfClosingFrom();
                pickDateDialog.show(getActivity().getSupportFragmentManager(), getString(R.string.open_dialog));
                break;
            }
            case R.id.requestDateOfClosingValueTo:{
                BottomSheetDialogFragmentSearchRequestPickDateOfClosingTo pickDateDialog = new BottomSheetDialogFragmentSearchRequestPickDateOfClosingTo();
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
