package com.example.admin.oracletest.ui.main.my_requests;

import dagger.android.support.DaggerFragment;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.R;
import com.example.admin.oracletest.dependencyinjection.app.SessionManager;
import com.example.admin.oracletest.models.EmployeeRequest;
import com.example.admin.oracletest.models.RequestsPage;
import com.example.admin.oracletest.models.User;
import com.example.admin.oracletest.ui.main.GetRequestsResource;
import com.example.admin.oracletest.ui.main.RequestsPageRecyclerAdapter;
import com.example.admin.oracletest.utils.RecyclerViewScrollListener;
import com.example.admin.oracletest.utils.RequestsPageRecyclerItemDecoration;
import com.example.admin.oracletest.viewmodel.ViewModelProviderFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Fragment with current/authenticated user requests
 */

public class MyRequestsFragment extends DaggerFragment {

    private static final String TAG = "MyRequestsFragment";

    // Виджеты
    public static RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

    // Injections
    @Inject
    ViewModelProviderFactory providerFactory;
    @Inject
    SessionManager sessionManager;

    // Переменные
    public Context context;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;
    public static int status_id = 1;  // новые заявки
    private static List<EmployeeRequest> requestList;
    private MyRequestsFragmentViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestList = new ArrayList<>();
        initViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_myrequests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUiComponents(view);
        get_emp_requests();
    }

    /*---------------------------------- Methods ------------------------------------*/

    /**
     * Init ui
     * @param view   fragment window
     */

    private void initUiComponents(View view){
        ColorDrawable colorDrawable = new ColorDrawable(getContext().getResources().getColor(R.color.kfuDefaultColor));
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.loadingText));
        progressDialog.setProgressDrawable(colorDrawable);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new RequestsPageRecyclerItemDecoration(10));
        recyclerView.setOnScrollListener(recyclerViewScrollListener);
    }

    /**
     * Init viewmodel
     */

    private void initViewModel(){
        viewModel = ViewModelProviders.of(this, providerFactory).get(MyRequestsFragmentViewModel.class);
    }

    /**
     * Get current/authenticated user requests from server
     */

    private void get_emp_requests(){
        showProgressDialog(true);
        viewModel.get_my_requests(getCurrentUserId(), currentPage, status_id, new Callback() {
            @Override
            public void result(Object data) {
                showProgressDialog(false);
                if(data != null){
                    RequestsPage requestsPage = (RequestsPage) data;
                    EmployeeRequest[] employeeRequests = requestsPage.getRequests();
                    if(employeeRequests.length > 0) {
                        List<EmployeeRequest> requests = new ArrayList<>(Arrays.asList(employeeRequests));
                        updateRecyclerView(requests);
                    }else{
                        showEmptyRequestsListAlertDialog();
                    }
                }else{
                    showEmptyRequestsListAlertDialog();
                }
            }
        });
    }

    private int getCurrentUserId(){
        return sessionManager.getAuthUser().getValue().data.getUserId();
    }

    /**
     * If user`s requests list is empty
     * therefore user ain`t got any requests
     * or it is the last page of requests with this status
     */

    private void showEmptyRequestsListAlertDialog(){
        String status = get_status_name(status_id);
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getContext());
        alertDialog.setTitle(context.getString(R.string.myRequests));
        alertDialog.setMessage("У Вас нет заявок со статусом '" + status + "'");
        alertDialog.setPositiveButton(context.getText(R.string.ok_button), (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    /**
     * If error happened
     */

    private void showServerErrorAlertDialog(){
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getContext());
        alertDialog.setMessage("Произошла ошибка. Обратитесь в тех.поддержку");
        alertDialog.setPositiveButton(getContext().getText(R.string.write_to_tech_support), (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    /**
     * Update recyclerview when load new requests from server
     * @param requests      new requests
     */

    private void updateRecyclerView(List<EmployeeRequest> requests){
        requestList.addAll(requests);
        RequestsPageRecyclerAdapter adapter = new RequestsPageRecyclerAdapter(requestList);
        recyclerView.setAdapter(adapter);
        showProgressDialog(false);
        isLoading = false;
        if(currentPage != 1) {
            recyclerView.smoothScrollToPosition(requestList.size() - 7);
        }
    }

    public static void smoothScrollToTopPosition(){
        recyclerView.smoothScrollToPosition(0);
    }

    private void showProgressDialog(boolean isVisible){
        if(isVisible){
            progressDialog.show();
        }else{
            progressDialog.dismiss();
        }
    }

    /**
     * Получить название статуса
     *
     * @param status_id     id статуса
     * @return              название статуса
     */

    public static String get_status_name(int status_id){
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
                resultStatusName = "Выполняются";
                break;
            }case 4:{
                resultStatusName = "Отменена";
                break;
            }case 5:{
                resultStatusName = "Приостановлена";
                break;
            }case 6:{
                resultStatusName = "Закрыта";
                break;
            }
        }
        return resultStatusName;
    }

    /**
     * Listener for recycler view
     * Whenever user got to the last request card
     * we load one more page of requests
     * So that way we perform PAGINATION PROCESS
     */

    RecyclerViewScrollListener recyclerViewScrollListener = new RecyclerViewScrollListener(linearLayoutManager) {
        @Override
        protected void loadMoreItems() {
            Log.d(TAG, "loadMoreItems: called");
            isLoading = true;
            currentPage += 1;
            // PERFORM PAGINATION
            if(User.myRequestsAmount >= 7) {
                get_emp_requests();
            }
            Log.d(TAG, "loadMoreItems: User.myRequestsAmount = " + User.myRequestsAmount);
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
    };

}
