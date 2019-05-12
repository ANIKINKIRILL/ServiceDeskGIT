package com.example.admin.oracletest.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.example.admin.oracletest.Activity.MainActivity;
import com.example.admin.oracletest.Models.EmployeeRequest;
import com.example.admin.oracletest.R;
import com.example.admin.oracletest.ViewModel.AllRequestsFragmentViewModel;

/**
 *  Фрагмент с настроками для посика и фильтрации заявок
 */

public class BottomSheetDialogFragmentFilterEmployeeRequests extends BottomSheetDialogFragment implements View.OnClickListener{

    private static final String TAG = "BottomSheetDialogFrag";

    // Виджеты
    private Spinner statusSpinner;
    private Button showResultButton;
    private EditText requestCodeEditText;

    // Переменные
    private AllRequestsFragmentViewModel viewModel;
    public static final int START_PAGE = 1;
    BottomSheetDialog dialog;
    private static int status_id = 1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initViewModel();
        dialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialog);
        return dialog;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        Log.d(TAG, "setupDialog: called");
        super.setupDialog(dialog, style);
        View view = View.inflate(getContext(), R.layout.bottomsheetdialogfragment_filter_employee_requests, null);
        dialog.setContentView(view);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View)view.getParent());
        bottomSheetBehavior.setHideable(false);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15,15,15,20);
        view.setLayoutParams(params);

        init(view);
        populateSpinners();

        statusSpinner.setSelection(status_id-1);

    }

    /**
     * Инициализация виджетов
     * @param view      окно фрагмента
     */

    private void init(View view){
        statusSpinner = view.findViewById(R.id.requestStatusSpinner);
        requestCodeEditText = view.findViewById(R.id.requestCodeEditText);
        showResultButton = view.findViewById(R.id.showResultButton);

        showResultButton.setOnClickListener(this);
    }

    /**
     * Получить {@link AllRequestsFragmentViewModel}
     */

    private void initViewModel(){
        viewModel = ViewModelProviders.of(this).get(AllRequestsFragmentViewModel.class);
    }

    /**
     * Наполнить спиннеры данными
     */

    private void populateSpinners(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                getContext().getResources().getStringArray(R.array.requestStatusArray));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
    }

    /**
     * Проверка на правильный ввод данных
     * в виджеты
     * @return      true -> успешно/ошибок нет
     *              false -> есть ошибки
     */

    private boolean isValid(){
        return true;
    }

    /**
     * Получить id статуса заявки
     *
     * @param statusName    название статуса
     * @return              id статуса
     */

    private int getStatusId(String statusName){
        int status_id = 1;
        switch (statusName){
            case EmployeeRequest.NEW:{
                status_id = 1;
                break;
            }
            case EmployeeRequest.DONE:{
                status_id = 2;
                break;
            }
            case EmployeeRequest.IN_PROGRESS:{
                status_id = 3;
                break;
            }
            case EmployeeRequest.CANCELED:{
                status_id = 4;
                break;
            }
            case EmployeeRequest.STOPED:{
                status_id = 5;
                break;
            }
            case EmployeeRequest.CLOSED:{
                status_id = 6;
                break;
            }
        }
        return status_id;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showResultButton:{
                if(requestCodeEditText.getText().toString().trim().isEmpty()){
                    // Сортируем по статусу заявки
                    String selectedStatus = statusSpinner.getSelectedItem().toString().toLowerCase();
                    status_id = getStatusId(selectedStatus);
                    AllRequestsFragment.DEFAULT_STATUS_ID = status_id;
                    MyRequestsFragment.DEFAULT_STATUS_ID = status_id;
                    MainActivity.createCounter++;
                    dialog.dismiss();
                    getActivity().recreate();
                }
                break;
            }
        }
    }
}









