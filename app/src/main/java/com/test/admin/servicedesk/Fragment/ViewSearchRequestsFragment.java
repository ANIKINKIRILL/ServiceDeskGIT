package com.test.admin.servicedesk.Fragment;

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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.test.admin.servicedesk.Activity.MainActivity;
import com.test.admin.servicedesk.Callback;
import com.test.admin.servicedesk.Models.EmployeeRequest;
import com.test.admin.servicedesk.R;
import com.test.admin.servicedesk.RecyclerViewScrollListener;
import com.test.admin.servicedesk.Utils.EmployeeRequestsRecyclerViewAdapter;
import com.test.admin.servicedesk.ViewModel.SearchRequestsFragmentViewModel;

import java.util.ArrayList;

// com.test.admin.servicedesk

/**
 * Фрагмент со списком найденных заявок
 * переход с {@link SearchRequestsFragment} после нажатия
 * на кнопку 'Показать результаты'
 */

public class ViewSearchRequestsFragment extends Fragment implements MenuItem.OnMenuItemClickListener{

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
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        initViewModel();
        configureActionBar();
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
        recyclerView = view.findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

         /*
             При скролле до последнего элемента
             загружается еще одна страница с заявками
         */

        recyclerView.addOnScrollListener(new RecyclerViewScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                get_employee_requests(mGetEmployeeRequestsCallback);
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

    /**
     * Инициализация {@link SearchRequestsFragmentViewModel}
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
                        // Скролл вниз до след. загруженного элемента
                        if(currentPage != 1) {
                            recyclerView.smoothScrollToPosition(ViewSearchRequestsFragment.requests.size() - 7);
                            Log.d(TAG, "onChanged: smooth scroll to " + Integer.toString(ViewSearchRequestsFragment.requests.size() - 7));
                        }
                    }
                }
            });
        }
    };

    /**
     * Метод пришёл с {@link SearchRequestsFragment} -> {@link MainActivity} -> сюда
     * @param requests  заявки которые нашлись 1 страница
     */

    public static void setRequests(ArrayList<EmployeeRequest> requests) {
        ViewSearchRequestsFragment.requests = requests;
    }

    /**
     * Метод пришёл с {@link SearchRequestsFragment} -> {@link MainActivity} -> сюда
     * @param p_sql_statement   SQL запрос на сервер
     * @param p_sql_statement_count_rows    SQL запрос на сервер для подсчета найденных строк
     */

    public static void setSqlParams(String p_sql_statement, String p_sql_statement_count_rows){
        ViewSearchRequestsFragment.p_sql_statement = p_sql_statement;
        ViewSearchRequestsFragment.p_sql_statement_count_rows = p_sql_statement_count_rows;
    }

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
