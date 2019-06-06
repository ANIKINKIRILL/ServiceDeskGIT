package com.example.admin.oracletest.ui.main.search;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.oracletest.R;
import com.example.admin.oracletest.models.EmployeeRequest;
import com.example.admin.oracletest.models.RequestsPage;
import com.example.admin.oracletest.models.User;
import com.example.admin.oracletest.ui.main.GetRequestsResource;
import com.example.admin.oracletest.ui.main.MainActivity;
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
 * When user push 'Показать' button in {@link SearchFragment}
 * then MainContainer is replaced with this Fragment
 * and user can see his searched requests on the screen
 */

public class ViewSearchFragment extends DaggerFragment implements MenuItem.OnMenuItemClickListener{

    // Injections
    @Inject
    ViewModelProviderFactory providerFactory;

    // Ui
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    // Vars
    public static List<EmployeeRequest> requests; /** список заявок с {@link SearchFragment} */
    public static String p_sql_statement;
    public static String p_sql_statement_count_rows;
    private SearchFragmentViewModel viewModel;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 1;
    private static List<EmployeeRequest> requestList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        requestList = new ArrayList<>();
        initViewModel();
        configureActionBar();
        return inflater.inflate(R.layout.fragment_viewsearchfragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        firstPopulationRecyclerViewWithData();
    }

    /**
     * Init {@link SearchFragmentViewModel}
     */

    private void initViewModel(){
        viewModel = ViewModelProviders.of(this, providerFactory).get(SearchFragmentViewModel.class);
    }

    /**
     * Настройка ActionBar
     */

    private void configureActionBar(){
        MainActivity.menu.findItem(R.id.clearSearchFilters).setVisible(false);
        MainActivity.menu.findItem(R.id.smoothScrollToTop).setVisible(true).setOnMenuItemClickListener(this);
    }

    /**
     * Инициализация виджетов
     * @param view      окно фрагмента
     */

    private void init(View view){
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
     * Наполнить RecyclerView первыми 7-ми заявками с
     * {@link SearchFragment}
     */

    private void firstPopulationRecyclerViewWithData(){
        RequestsPageRecyclerAdapter adapter = new RequestsPageRecyclerAdapter(ViewSearchFragment.requests);
        requestList.addAll(ViewSearchFragment.requests);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Get next page of requests
     */

    private void searchRequests(){
        progressDialog.show();
        viewModel.searchRequests(ViewSearchFragment.p_sql_statement, ViewSearchFragment.p_sql_statement_count_rows, currentPage).observe(this, new Observer<GetRequestsResource<RequestsPage>>() {
            @Override
            public void onChanged(@Nullable GetRequestsResource<RequestsPage> requestsPageGetRequestsResource) {
                if(requestsPageGetRequestsResource != null){
                    switch (requestsPageGetRequestsResource.status){
                        case ERROR:{
                            progressDialog.dismiss();
                            showErrorAlertDialog();
                            break;
                        }
                        case SUCCESS:{
                            progressDialog.dismiss();
                            List<EmployeeRequest> requests = new ArrayList<>(Arrays.asList(requestsPageGetRequestsResource.data.getRequests()));
                            requestList.addAll(requests);
                            RequestsPageRecyclerAdapter adapter =
                                    new RequestsPageRecyclerAdapter(requestList);
                            recyclerView.setAdapter(adapter);
                            isLoading = false;
                            // Скролл вниз до след. загруженного элемента
                            if(currentPage != 1) {
                                recyclerView.smoothScrollToPosition(requestList.size() - 7);
                            }
                            break;
                        }
                    }
                }
            }
        });
    }

    /**
     * If we ain`t got more requests
     */

    private void showErrorAlertDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Найденные заявки");
        alertDialog.setMessage("Заявок больше не осталось");
        alertDialog.setPositiveButton(getContext().getText(R.string.ok_button), (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }


    /**
     * Метод пришёл с {@link SearchFragment} -> {@link com.example.admin.oracletest.ui.main.MainActivity} -> сюда
     * @param requests  заявки которые нашлись 1 страница
     */

    public static void setRequests(List<EmployeeRequest> requests) {
        ViewSearchFragment.requests = requests;
    }

    /**
     * Метод пришёл с {@link SearchFragment} -> {@link com.example.admin.oracletest.ui.main.MainActivity} -> сюда
     * @param p_sql_statement   SQL запрос на сервер
     * @param p_sql_statement_count_rows    SQL запрос на сервер для подсчета найденных строк
     */

    public static void setSqlParams(String p_sql_statement, String p_sql_statement_count_rows){
        ViewSearchFragment.p_sql_statement = p_sql_statement;
        ViewSearchFragment.p_sql_statement_count_rows = p_sql_statement_count_rows;
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
            if(User.search_requests_amount >= 7) {
                searchRequests();
            }
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


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.smoothScrollToTop:{
                recyclerView.smoothScrollToPosition(0);
                break;
            }
        }
        return true;
    }
}
