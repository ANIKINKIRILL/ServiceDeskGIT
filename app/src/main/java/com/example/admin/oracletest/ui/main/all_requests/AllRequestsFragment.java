package com.example.admin.oracletest.ui.main.all_requests;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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

import com.example.admin.oracletest.R;
import com.example.admin.oracletest.models.EmployeeRequest;
import com.example.admin.oracletest.models.RequestsPage;
import com.example.admin.oracletest.ui.main.GetRequestsResource;
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
    private RecyclerView recyclerView;

    // Vars
    private AllRequestsViewModel viewModel;
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
        return inflater.inflate(R.layout.fragment_all_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUiComponents(view);
        get_current_requests();
    }

    /*--------------------- Methods ----------------------- */


    private void initViewModel(){
        viewModel = ViewModelProviders.of(this, providerFactory).get(AllRequestsViewModel.class);
    }

    private void initUiComponents(View view){
        ColorDrawable colorDrawable = new ColorDrawable(getContext().getResources().getColor(R.color.kfuDefaultColor));
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.loadingText));
        progressDialog.setProgressDrawable(colorDrawable);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
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
        viewModel.get_current_requests(currentPage, status_id).observe(getViewLifecycleOwner(), new Observer<GetRequestsResource<RequestsPage>>() {
            @Override
            public void onChanged(@Nullable GetRequestsResource<RequestsPage> requestsPageGetRequestsResource) {
                assert requestsPageGetRequestsResource != null;
                switch (requestsPageGetRequestsResource.status){
                    case SUCCESS:{
                        Log.d(TAG, "onChanged: SUCCESS");
                        List<EmployeeRequest> requests = new ArrayList<>(Arrays.asList(requestsPageGetRequestsResource.data.getRequests()));
                        requestList.addAll(requests);
                        RequestsPageRecyclerAdapter adapter = new RequestsPageRecyclerAdapter(requestList);
                        recyclerView.setAdapter(adapter);
                        showProgressDialog(false);
                        isLoading = false;
                        if(currentPage != 1) {
                            recyclerView.smoothScrollToPosition(requestList.size() - 7);
                        }
                        break;
                    }
                    case ERROR:{
                        Log.d(TAG, "onChanged: error");
                        break;
                    }
                }
            }
        });
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
