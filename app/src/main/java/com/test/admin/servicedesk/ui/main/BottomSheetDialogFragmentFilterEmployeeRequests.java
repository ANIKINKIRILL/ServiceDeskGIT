package com.test.admin.servicedesk.ui.main;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.test.admin.servicedesk.R;
import com.test.admin.servicedesk.models.EmployeeRequest;
import com.test.admin.servicedesk.ui.main.all_requests.AllRequestsFragment;
import com.test.admin.servicedesk.ui.main.my_requests.MyRequestsFragment;
import com.test.admin.servicedesk.viewmodel.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatDialogFragment;

/**
 *  Фрагмент с настроками для посика и фильтрации заявок
 */

public class BottomSheetDialogFragmentFilterEmployeeRequests extends DaggerAppCompatDialogFragment implements View.OnClickListener {

    private static final String TAG = "BottomSheetDialogFrag";

    // Ui
    private Spinner statusSpinner;
    private Button showResultButton;

    // Vars
    public static final int START_PAGE = 1;
    BottomSheetDialog dialog;
    private static int status_id = 1;

    // Injections
    @Inject
    ViewModelProviderFactory providerFactory;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialog);
        return dialog;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view = View.inflate(getContext(), R.layout.bottomsheetdialogfragment_filter_employee_requests, null);
        dialog.setContentView(view);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setHideable(false);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(15, 15, 15, 20);
        view.setLayoutParams(params);

        init(view);
        populateSpinners();

        statusSpinner.setSelection(status_id - 1);

    }

    /**
     * Инициализация виджетов
     *
     * @param view окно фрагмента
     */

    private void init(View view) {
        statusSpinner = view.findViewById(R.id.requestStatusSpinner);
        showResultButton = view.findViewById(R.id.showResultButton);

        showResultButton.setOnClickListener(this);
    }

    /**
     * Наполнить спиннеры данными
     */

    private void populateSpinners() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                getContext().getResources().getStringArray(R.array.requestStatusArray));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
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
        switch (v.getId()) {
            case R.id.showResultButton: {
                String selectedStatus = statusSpinner.getSelectedItem().toString().toLowerCase();
                status_id = getStatusId(selectedStatus);
                AllRequestsFragment.status_id = status_id;
                MyRequestsFragment.status_id = status_id;
                MainActivity.createCounter++;
                dialog.dismiss();
                getActivity().recreate();
                break;
            }
        }
    }
}
