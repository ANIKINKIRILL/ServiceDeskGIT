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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.oracletest.Activity.MainActivity;
import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.Models.EmployeeRequest;
import com.example.admin.oracletest.Models.User;
import com.example.admin.oracletest.R;
import com.example.admin.oracletest.RecyclerViewScrollListener;
import com.example.admin.oracletest.Settings;
import com.example.admin.oracletest.Utils.EmployeeRequestsRecyclerViewAdapter;
import com.example.admin.oracletest.ViewModel.MyRequestsFragmentViewModel;

import java.util.ArrayList;

/**
 * Фрагмент с заяквами исполнителя
 */

public class MyRequestsFragment extends Fragment implements MenuItem.OnMenuItemClickListener{

    private static final String TAG = "MyRequestsFragment";

    // Виджеты
    private static RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private LinearLayoutManager linearLayoutManager;

    // Переменные
    private Context context;
    private MyRequestsFragmentViewModel viewModel;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;
    private static ArrayList<EmployeeRequest> requestList;
    public static int DEFAULT_STATUS_ID = 1;  // новые заявки

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestList = new ArrayList<>();
        initViewModel();
        get_employee_requests(mGetEmployeeRequestsCallback, Settings.getUserId(), DEFAULT_STATUS_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myrequests, container, false);
        init(view);
        context = container.getContext().getApplicationContext();
        return view;
    }

    /**
     * Инициализация виджетов
     * @param view      окно фрагмента
     */

    private void init(View view){
        recyclerView = view.findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerViewScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                performPagination(mGetEmployeeRequestsCallback, Settings.getUserId(), DEFAULT_STATUS_ID);
            }

            @Override
            public int getTotalPageCount() {
                return 5;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

    }

    private void performPagination(Callback callback, String u_id, int status_id){
        if(User.current_requests_amount > 7) {
            get_employee_requests(callback, u_id, status_id);
        }
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

    private void get_employee_requests(Callback callback, String u_id, int status_id){
        ColorDrawable colorDrawable = new ColorDrawable(getContext().getResources().getColor(R.color.kfuDefaultColor));
        // Показать загрузочное окно
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.loadingText));
        progressDialog.setProgressDrawable(colorDrawable);
        progressDialog.show();

        viewModel.get_requests(getContext(), u_id, currentPage, status_id, callback);
    }

    /**
     * Получить название статуса
     *
     * @param status_id     id статуса
     * @return              название статуса
     */

    private String get_status_name(int status_id){
        String resultStatusName = "Новая";
        switch (status_id){
            case 1:{
                resultStatusName = "Новая";
                break;
            }
            case 2:{
                resultStatusName = "Выполнена";
                break;
            }case 3:{
                resultStatusName = "Выполнются";
                break;
            }case 4:{
                resultStatusName = "Отменена";
                break;
            }case 5:{
                resultStatusName = "Приостановлена";
                break;
            }case 6:{
                resultStatusName = "Закрта";
                break;
            }
        }
        return resultStatusName;
    }

    /**
     * Callback, который вернется после получения заявок на исполнителя (externalCallbackGetRequests)
     */
    
    Callback mGetEmployeeRequestsCallback = new Callback() {
        @Override
        public void execute(Object data) {
            LiveData<ArrayList<EmployeeRequest>> requests = (LiveData<ArrayList<EmployeeRequest>>) data;
            // View observes любые изменения в LiveData и реагирует на них
            requests.observe(MyRequestsFragment.this, new Observer<ArrayList<EmployeeRequest>>() {
                @Override
                public void onChanged(@Nullable ArrayList<EmployeeRequest> employeeRequests) {
                    requestList.addAll(employeeRequests);
                    // Если у пользователя нет заявок с этим статусом
                    if(User.current_requests_amount == 0 || requests.getValue().size() == 0){
                        String status = get_status_name(DEFAULT_STATUS_ID);
                        progressDialog.dismiss();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setTitle(context.getString(R.string.myRequests));
                        alertDialog.setMessage("У Вас нет заявок со статусом '" + status + "'");
                        alertDialog.setPositiveButton(context.getText(R.string.ok_button), (dialog, which) -> dialog.dismiss());
                        alertDialog.show();
                    }
                    // Количетсво заявок больше чем на одну страницу
                    if(User.current_requests_amount >= 7){
                        progressDialog.dismiss();
                        // Создаем адаптер и RecyclerView для отображения заявок
                        EmployeeRequestsRecyclerViewAdapter adapter =
                                new EmployeeRequestsRecyclerViewAdapter(getContext(), requestList);
                        recyclerView.setAdapter(adapter);
                        if(currentPage != 1) {
                            recyclerView.smoothScrollToPosition(requestList.size() - 7);
                            Log.d(TAG, "onChanged: smooth scroll to " + Integer.toString(requestList.size() - 7));
                        }
                        isLoading = false;
                        }
                    // Количетсво заявок меньше чем на одну страницу
                    if(User.current_requests_amount < 7){
                        progressDialog.dismiss();
                        // Создаем адаптер и RecyclerView для отображения заявок
                        EmployeeRequestsRecyclerViewAdapter adapter =
                                new EmployeeRequestsRecyclerViewAdapter(getContext(), requestList);
                        recyclerView.setAdapter(adapter);
                        isLoading = false;
                    }

                }
            });
        }
    };

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.smoothScrollToTop:{
                recyclerView.smoothScrollToPosition(1);
                break;
            }
        }
        return true;
    }
}
