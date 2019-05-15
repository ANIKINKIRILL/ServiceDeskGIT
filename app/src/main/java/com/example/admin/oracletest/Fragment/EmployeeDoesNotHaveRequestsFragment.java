package com.example.admin.oracletest.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.oracletest.R;

import java.util.Arrays;


/**
 * Фрагмент с сообщением, что у исполнителя нет заявок
 */

public class EmployeeDoesNotHaveRequestsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_does_not_have_requests, container, false);
        return view;
    }

    private void test(){
//        int a[] = {1,2,3,4};
//        Arrays.stream(a).filter(el -> el % 2 != 0).forEach(System.out::println);
    }

}
