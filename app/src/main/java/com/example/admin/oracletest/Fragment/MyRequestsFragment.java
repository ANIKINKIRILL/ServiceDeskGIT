package com.example.admin.oracletest.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.drm.ProcessedData;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.EmployeeRequestsRecyclerViewAdapter;
import com.example.admin.oracletest.Models.EmployeeRequest;
import com.example.admin.oracletest.Models.User;
import com.example.admin.oracletest.R;
import com.example.admin.oracletest.Settings;

import java.util.ArrayList;

/**
 * Фрагмент с заяквами исполнителя
 */

public class MyRequestsFragment extends Fragment {

    private static final String TAG = "MyRequestsFragment";

    // Виджеты
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myrequests, container, false);
        init(view);
        get_employee_requests(mGetEmployeeRequestsCallback, Settings.getUserId());
        return view;
    }

    /**
     * Инициализация виджетов
     * @param view      окно фрагмента
     */

    private void init(View view){
        recyclerView = view.findViewById(R.id.recycler_view);
    }

    /**
     * Получить заявки исполнителя
     * @param callback      Callback, который вернется после получения заявок на исполнителя
     * @param u_id          id исполнителя
     */

    private void get_employee_requests(Callback callback, String u_id){
        // Показать загрузочное окно
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getResources().getString(R.string.loadingText));
        progressDialog.show();
        User.get_requests(u_id, callback);
    }

    /**
     * Callback, который вернется после получения заявок на исполнителя (externalCallbackGetRequests)
     */

    Callback mGetEmployeeRequestsCallback = new Callback() {
        @Override
        public void execute(Object data) {
            ArrayList<EmployeeRequest> requests = (ArrayList) data;
            Log.d(TAG, "employee_requests: " + requests.size());
            if(requests.size() == 0){
                progressDialog.dismiss();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setMessage(getContext().getResources().getString(R.string.noRequestsText));
            }else{
                // Создаем адаптер и RecyclerView для отображения заявок
                EmployeeRequestsRecyclerViewAdapter adapter = new EmployeeRequestsRecyclerViewAdapter(requests, getContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }
        }
    };

    /**
     * Замена FrameLayout на Fragment
     * @param fragment Fragment который поменятся c FrameLayout
     */

    private void doFragmentTransaction(Fragment fragment){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.commit();
    }

}
