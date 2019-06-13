package com.example.admin.oracletest.ui.main.search;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.admin.oracletest.R;

/**
 * Fragment with date picker for {@link SearchFragment}
 */

public class BottomSheetDialogFragmentSearchRequestPickRegDate extends BottomSheetDialogFragment implements View.OnClickListener {

    private static final String TAG = "BotSheetSerReqPickDate";

    // UI
    private NumberPicker dayPicker, monthPicker, yearPicker;
    private TextView okButton, deleteButton;

    // Vars
    private static String date = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomsheetdialogfragment_searchrequest_pickdate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dayPicker = view.findViewById(R.id.dayPicker);
        monthPicker = view.findViewById(R.id.monthPicker);
        yearPicker = view.findViewById(R.id.yearPicker);
        okButton = view.findViewById(R.id.okButton);
        deleteButton = view.findViewById(R.id.deleteButton);

        okButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        setNumberPickersConfigurationSettings();
    }

    /**
     * Set basic settings for number pickers
     * as min, max value and others
     */

    private void setNumberPickersConfigurationSettings(){
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);
        dayPicker.setWrapSelectorWheel(true);

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setWrapSelectorWheel(true);

        yearPicker.setMinValue(2008);
        yearPicker.setMaxValue(2030);
        yearPicker.setValue(2019);
        yearPicker.setWrapSelectorWheel(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.okButton:{
                date = String.format("%d/%d/%d", dayPicker.getValue(), monthPicker.getValue(), yearPicker.getValue());
                SearchFragment.setReg_date(date);
                Log.d(TAG, "onClick: pickedDate" + date);
                dismiss();
                break;
            }
            case R.id.deleteButton:{
                date = "";
                SearchFragment.setReg_date(date);
                dismiss();
                break;
            }
        }

    }
}
