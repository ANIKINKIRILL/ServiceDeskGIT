package com.example.admin.oracletest.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.Utils.EmployeeRequestsRecyclerViewAdapter;
import com.example.admin.oracletest.Models.EmployeeRequest;
import com.example.admin.oracletest.Models.User;
import com.example.admin.oracletest.R;
import com.example.admin.oracletest.Settings;
import com.example.admin.oracletest.ViewModel.AuthActivityViewModel;
import com.example.admin.oracletest.ViewModel.MyRequestsFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Фрагмент с заяквами исполнителя
 */

public class MyRequestsFragment extends Fragment {

    private static final String TAG = "MyRequestsFragment";

    // Виджеты
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    // Переменные
    private Context context;
    private MyRequestsFragmentViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myrequests, container, false);
        initViewModel();
        init(view);
        context = container.getContext().getApplicationContext();
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
     * Инициализация {@link MyRequestsFragmentViewModel}
     */

    private void initViewModel(){
        viewModel = ViewModelProviders.of(MyRequestsFragment.this).get(MyRequestsFragmentViewModel.class);
    }

    /**
     * Получить заявки исполнителя
     * @param callback      Callback, который вернется после получения заявок на исполнителя
     * @param u_id          id исполнителя
     */

    private void get_employee_requests(Callback callback, String u_id){
        ColorDrawable colorDrawable = new ColorDrawable(getContext().getResources().getColor(R.color.kfuDefaultColor));
        // Показать загрузочное окно
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(context.getString(R.string.loadingText));
        progressDialog.setProgressDrawable(colorDrawable);
        progressDialog.show();
        /*
            Просим ViewModel сделать запрос на Repository, где второй
            идет на Сервер и возвращает JSON. ViewModel вся основная бизнесс
            логика (парсинг JSON), далее ViewModel возвращает LiveData на
            {@link MyRequestsFragment} => UI будет обновляться автоматически.
            Так как View observes любые изменения в LiveData и реагирует на них
        */
        viewModel.get_requests(getContext(), u_id, callback);
    }

    /**
     * Callback, который вернется после получения заявок на исполнителя (externalCallbackGetRequests)
     */

    Callback mGetEmployeeRequestsCallback = new Callback() {
        @Override
        public void execute(Object data) {
            LiveData<ArrayList<EmployeeRequest>> requests = (LiveData<ArrayList<EmployeeRequest>>) data;
            Log.d(TAG, "employee_requests: " + requests.getValue().size());
            // View observes любые изменения в LiveData и реагирует на них
            requests.observe(MyRequestsFragment.this, new Observer<ArrayList<EmployeeRequest>>() {
                @Override
                public void onChanged(@Nullable ArrayList<EmployeeRequest> employeeRequests) {
                    // Если у пользователя нет заявок
                    if(employeeRequests.size() == 0){
                        progressDialog.dismiss();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setTitle(context.getString(R.string.myRequests));
                        alertDialog.setMessage(context.getString(R.string.noRequestsText));
                        alertDialog.setPositiveButton(context.getText(R.string.ok_button), (dialog, which) -> dialog.dismiss());
                        alertDialog.show();
                    }else{
                        progressDialog.dismiss();
                        // Создаем адаптер и RecyclerView для отображения заявок
                        EmployeeRequestsRecyclerViewAdapter adapter =
                                new EmployeeRequestsRecyclerViewAdapter(getContext(), employeeRequests);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setAdapter(adapter);
                    }
                }
            });
        }
    };
}
