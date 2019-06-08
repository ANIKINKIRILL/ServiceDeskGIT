package com.example.admin.oracletest.ui.main.all_requests;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.R;
import com.example.admin.oracletest.models.EmployeeRequest;
import com.example.admin.oracletest.models.RequestsPage;
import com.example.admin.oracletest.ui.main.RequestsPageRecyclerAdapter;
import com.example.admin.oracletest.utils.RecyclerViewScrollListener;
import com.example.admin.oracletest.utils.RequestsPageRecyclerItemDecoration;
import com.example.admin.oracletest.viewmodel.ViewModelProviderFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

/**
 * Fragment with all ServiceDesk requests
 */

public class AllRequestsFragment extends DaggerFragment {

    private static final String TAG = "AllRequestsFragment";

    // Injections
    @Inject
    ViewModelProviderFactory providerFactory;

    // Ui
    private ProgressDialog progressDialog;
    private static RecyclerView recyclerView;

    // Vars
    private AllRequestsFragmentViewModel viewModel;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    private boolean isLoading = false;
    private int currentPage = 1;
    private boolean isLastPage = false;
    public static int status_id = 1;  // новые заявки
    public static List<EmployeeRequest> requestList;

    /*--------------------- Lifecycle ----------------------- */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestList = new ArrayList<>();
        initViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: called");
        return inflater.inflate(R.layout.fragment_all_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: called");
        initUiComponents(view);
        get_current_requests();
    }

    /*--------------------- Methods ----------------------- */

    private void initViewModel(){
        viewModel = ViewModelProviders.of(this, providerFactory).get(AllRequestsFragmentViewModel.class);
    }

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

    private void showProgressDialog(boolean isVisible){
        if(isVisible){
            progressDialog.show();
        }else{
            progressDialog.dismiss();
        }
    }

    /**
     * Get current requests from server
     */

    private void get_current_requests(){
        showProgressDialog(true);
        viewModel.get_current_requests(currentPage, status_id, new Callback() {
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
                    showServerErrorAlertDialog();
                }
            }
        });
    }

    /**
     * If we ain`t got more requests
     */

    private void showEmptyRequestsListAlertDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Найденные заявки");
        alertDialog.setMessage("Заявок больше не осталось");
        alertDialog.setPositiveButton(getContext().getText(R.string.ok_button), (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    /**
     * If error happened
     */

    private void showServerErrorAlertDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
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
            get_current_requests();
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
