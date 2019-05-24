package com.example.admin.oracletest.Fragment;

import android.app.ActionBar;
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
import com.example.admin.oracletest.Models.User;
import com.example.admin.oracletest.OnViewSearchRequestsFragmentListener;
import com.example.admin.oracletest.R;
import com.example.admin.oracletest.RecyclerViewScrollListener;
import com.example.admin.oracletest.Settings;
import com.example.admin.oracletest.Utils.EmployeeRequestsRecyclerViewAdapter;
import com.example.admin.oracletest.ViewModel.MyRequestsFragmentViewModel;
import com.example.admin.oracletest.ViewModel.SearchRequestsFragmentViewModel;

import java.util.ArrayList;

/**
 * Фрагмент со списком найденных заявок
 */

public class ViewSearchRequestsFragment extends Fragment{

    private static final String TAG = "ViewSearchRequestsFrag";

    // Виджеты
    private static RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private LinearLayoutManager linearLayoutManager;

    // Переменные
    private SearchRequestsFragmentViewModel viewModel;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;
    public static ArrayList<EmployeeRequest> requests; /** список заявок с {@link SearchRequestsFragment} */
    public static String p_sql_statement;
    public static String p_sql_statement_count_rows;
    private static Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        initViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myrequests, container, false);
        init(view);
        firstPopulationRecyclerViewWithData();
        return view;
    }

    /**
     * Наполнить RecyclerView первыми 7-ми заявками с
     * {@link SearchRequestsFragment}
     */

    private void firstPopulationRecyclerViewWithData(){
        EmployeeRequestsRecyclerViewAdapter adapter = new EmployeeRequestsRecyclerViewAdapter(context, ViewSearchRequestsFragment.requests);
        recyclerView.setAdapter(adapter);
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
                performPagination(mGetEmployeeRequestsCallback);
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


    private void performPagination(Callback callback){
        get_employee_requests(callback);
    }

    /**
     * Инициализация {@link MyRequestsFragmentViewModel}
     */

    private void initViewModel(){
        viewModel = ViewModelProviders.of(ViewSearchRequestsFragment.this).get(SearchRequestsFragmentViewModel.class);
    }

    /**
     * Получить заявки исполнителя
     * @param callback      Callback, который вернется после получения заявок на исполнителя
     */

    private void get_employee_requests(Callback callback){
        ColorDrawable colorDrawable = new ColorDrawable(getContext().getResources().getColor(R.color.kfuDefaultColor));
        // Показать загрузочное окно
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getContext().getString(R.string.loadingText));
        progressDialog.setProgressDrawable(colorDrawable);
        progressDialog.show();
        viewModel.search_request(p_sql_statement, p_sql_statement_count_rows, currentPage, callback, getContext());
    }

    /**
     * Callback, который вернется после получения заявок на исполнителя (externalCallbackGetRequests)
     */

    Callback mGetEmployeeRequestsCallback = new Callback() {
        @Override
        public void execute(Object data) {
            progressDialog.dismiss();
            LiveData<ArrayList<EmployeeRequest>> requests = (LiveData<ArrayList<EmployeeRequest>>) data;
            requests.observe(ViewSearchRequestsFragment.this, new Observer<ArrayList<EmployeeRequest>>() {
                @Override
                public void onChanged(@Nullable ArrayList<EmployeeRequest> employeeRequests) {
                    Log.d(TAG, "onChanged: " + employeeRequests.size());
                    ViewSearchRequestsFragment.requests.addAll(employeeRequests);
                    if(employeeRequests.size() == 0){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                        alertDialog.setTitle("Найденные заявки");
                        alertDialog.setMessage("Заявок больше не осталось");
                        alertDialog.setPositiveButton(getContext().getText(R.string.ok_button), (dialog, which) -> dialog.dismiss());
                        alertDialog.show();
                    }else {
                        EmployeeRequestsRecyclerViewAdapter adapter =
                                new EmployeeRequestsRecyclerViewAdapter(getContext(), ViewSearchRequestsFragment.requests);
                        recyclerView.setAdapter(adapter);
                        isLoading = false;
                        if(currentPage != 1) {
                            recyclerView.smoothScrollToPosition(ViewSearchRequestsFragment.requests.size() - 7);
                            Log.d(TAG, "onChanged: smooth scroll to " + Integer.toString(ViewSearchRequestsFragment.requests.size() - 7));
                        }
                    }
                }
            });
        }
    };

    public static void setRequests(ArrayList<EmployeeRequest> requests) {
        ViewSearchRequestsFragment.requests = requests;
    }

    public static void setSqlParams(String p_sql_statement, String p_sql_statement_count_rows){
        ViewSearchRequestsFragment.p_sql_statement = p_sql_statement;
        ViewSearchRequestsFragment.p_sql_statement_count_rows = p_sql_statement_count_rows;
    }

}
