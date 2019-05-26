package com.test.admin.servicedesk.Fragment.AllRequests;

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

import com.test.admin.servicedesk.Callback;
import com.test.admin.servicedesk.Models.EmployeeRequest;
import com.test.admin.servicedesk.R;
import com.test.admin.servicedesk.RecyclerViewScrollListener;
import com.test.admin.servicedesk.Utils.EmployeeRequestsRecyclerViewAdapter;
import com.test.admin.servicedesk.ViewModel.AllRequestsFragmentViewModel;

import java.util.ArrayList;

/**
 * Фрагмент со всеми заявками ServiceDesk
 *
 */

public class AllRequestsFragment extends Fragment{

    private static final String TAG = "AllRequestsFragment";
    
    // Виджеты
    private RecyclerView recyclerView;

    // Переменные
    private ProgressDialog progressDialog;
    private LinearLayoutManager linearLayoutManager;
    private AllRequestsFragmentViewModel viewModel;
    private Context context;
    public static ArrayList<EmployeeRequest> requestList;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;
    public static int DEFAULT_STATUS_ID = 1;  // новые заявки

    /*-------------------------- LIFECYCLE --------------------------*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: called " + DEFAULT_STATUS_ID);
        requestList = new ArrayList<>();
        initViewModel();
        get_current_requests(currentPage, DEFAULT_STATUS_ID, mGetCurrentRequestsCallback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: called" + DEFAULT_STATUS_ID);
        View view = inflater.inflate(R.layout.fragment_all_requests, container, false);
        init(view);
        context = container.getContext().getApplicationContext();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: called" + DEFAULT_STATUS_ID);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: called" + DEFAULT_STATUS_ID);
    }

    /*---------------------------- Class Methods ----------------------------*/

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

    public void get_current_requests(int page_number, int status_id, Callback callback){
        ColorDrawable colorDrawable = new ColorDrawable(getContext().getResources().getColor(R.color.kfuDefaultColor));
        // Показать загрузочное окно
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.loadingText));
        progressDialog.setProgressDrawable(colorDrawable);
        progressDialog.show();
        viewModel.get_current_requests(getContext(), page_number, status_id, callback, contactView);
    }

    /**
     * Callback, который вернется после получения заявок на исполнителя (externalCallbackGetRequests)
     */

    public Callback mGetCurrentRequestsCallback = new Callback() {
        @Override
        public void execute(Object data) {
            Log.d(TAG, "execute: called");
            LiveData<ArrayList<EmployeeRequest>> requests = (LiveData<ArrayList<EmployeeRequest>>) data;
            // View observes любые изменения в LiveData и реагирует на них
            requests.observe(AllRequestsFragment.this, new Observer<ArrayList<EmployeeRequest>>() {
                @Override
                public void onChanged(@Nullable ArrayList<EmployeeRequest> employeeRequests) {
                    requestList.addAll(employeeRequests);
                    progressDialog.cancel();
                }
            });
        }
    };

    /*----------------------- CONTACT -----------------------*/

    private AllRequestsFragmentContact.View contactView = new AllRequestsFragmentContact.View() {
        @Override
        public void userDoesNotHaveRequests() {
            // Если у пользователя нет заявок
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
            alertDialog.setTitle(context.getString(R.string.myRequests));
            alertDialog.setMessage(context.getString(R.string.noRequestsText));
            alertDialog.setPositiveButton(context.getText(R.string.ok_button), (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        }

        @Override
        public void userHasSomeRequests() {
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
    };

}
