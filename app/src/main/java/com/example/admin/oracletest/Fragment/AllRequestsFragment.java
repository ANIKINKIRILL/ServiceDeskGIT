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
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.Models.EmployeeRequest;
import com.example.admin.oracletest.R;
import com.example.admin.oracletest.RecyclerViewScrollListener;
import com.example.admin.oracletest.Settings;
import com.example.admin.oracletest.Utils.EmployeeRequestsRecyclerViewAdapter;
import com.example.admin.oracletest.ViewModel.AllRequestsFragmentViewModel;

import java.util.ArrayList;

public class AllRequestsFragment extends Fragment {

    // Виджеты
    private RecyclerView recyclerView;

    // Переменные
    private ProgressDialog progressDialog;
    private LinearLayoutManager linearLayoutManager;
    private AllRequestsFragmentViewModel viewModel;
    private Context context;
    private static ArrayList<EmployeeRequest> requestList = new ArrayList<>();
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;
    public static final int DEFAULT_STATUS_ID = 1;  // новые заявки

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_requests, container, false);
        initViewModel();
        init(view);
        context = container.getContext().getApplicationContext();
        get_current_requests(currentPage, DEFAULT_STATUS_ID, mGetCurrentRequestsCallback);
        return view;
    }

    /**
     * Найти ui компоненты
     * @param view  на чем находить
     */

    private void init(View view){
        recyclerView = view.findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(new RecyclerViewScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                performPagination(currentPage, DEFAULT_STATUS_ID, mGetCurrentRequestsCallback);
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

    private void performPagination(int page_number, int status_id, Callback callback){
        get_current_requests(page_number, status_id, callback);
    }

    /**
     * Получить {@link AllRequestsFragmentViewModel}
     */

    private void initViewModel(){
        viewModel = ViewModelProviders.of(this).get(AllRequestsFragmentViewModel.class);
    }

    private void get_current_requests(int page_number, int status_id, Callback callback){
        ColorDrawable colorDrawable = new ColorDrawable(getContext().getResources().getColor(R.color.kfuDefaultColor));
        // Показать загрузочное окно
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(context.getString(R.string.loadingText));
        progressDialog.setProgressDrawable(colorDrawable);
        progressDialog.show();
        viewModel.get_current_requests(getContext(), page_number, status_id, callback);
    }

    /**
     * Callback, который вернется после получения заявок на исполнителя (externalCallbackGetRequests)
     */

    Callback mGetCurrentRequestsCallback = new Callback() {
        @Override
        public void execute(Object data) {
            LiveData<ArrayList<EmployeeRequest>> requests = (LiveData<ArrayList<EmployeeRequest>>) data;
            // View observes любые изменения в LiveData и реагирует на них
            requests.observe(AllRequestsFragment.this, new Observer<ArrayList<EmployeeRequest>>() {
                @Override
                public void onChanged(@Nullable ArrayList<EmployeeRequest> employeeRequests) {
                    requestList.addAll(employeeRequests);
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
                                new EmployeeRequestsRecyclerViewAdapter(getContext(), requestList);
                        recyclerView.setAdapter(adapter);
                        isLoading = false;
                    }
                }
            });
        }
    };

}
