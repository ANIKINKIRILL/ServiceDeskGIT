package com.test.admin.servicedesk.Activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.test.admin.servicedesk.Fragment.AllRequests.AllRequestsFragment;
import com.test.admin.servicedesk.Fragment.BottomSheetDialogFragmentFilterEmployeeRequests;
import com.test.admin.servicedesk.Fragment.MyRequests.MyRequestsFragment;
import com.test.admin.servicedesk.Fragment.SearchRequestsFragment.SearchRequestsFragment;
import com.test.admin.servicedesk.Fragment.SettingsFragment.SettingsFragment;
import com.test.admin.servicedesk.Fragment.SearchRequestsFragment.ViewSearchRequestsFragment;
import com.test.admin.servicedesk.Models.EmployeeRequest;
import com.test.admin.servicedesk.Models.User;
import com.test.admin.servicedesk.Fragment.SearchRequestsFragment.OnViewSearchRequestsFragmentListener;
import com.test.admin.servicedesk.Fragment.SearchRequestsFragment.OnViewSearchRequestsFragmentSqlParams;
import com.test.admin.servicedesk.R;

import java.util.ArrayList;

/**
 * Главное Активити
 */

public class MainActivity extends AppCompatActivity implements OnViewSearchRequestsFragmentListener,
        OnViewSearchRequestsFragmentSqlParams{

    private static final String TAG = "MainActivity";

    // Виджеты
    BottomNavigationView bottomNavigationView;
    FrameLayout mainContainer;
    public static Menu menu;


    // Переменные
    public static ActionBar actionBar;
    public static int createCounter = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitymain);
        init();
        initActionBar();
        if(AllRequestsFragment.DEFAULT_STATUS_ID == 1 && createCounter == 0) {
            setLaunchFragment();
        }
    }

    /**
     * Инициализация
     */

    private void init(){
        bottomNavigationView = findViewById(R.id.main_bottom_nav_view);
        mainContainer = findViewById(R.id.main_container);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    /**
     * Инициализация ActionBar
     */

    private void initActionBar(){
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.kfuDefaultColor)));
        actionBar.setElevation(0);
    }

    /**
     * Установка самого первого фрагмента при запуске MainActivity
     */

    private void setLaunchFragment(){
        doFragmentTransaction(new AllRequestsFragment(), getString(R.string.allRequests));
    }

    /**
     * Слушатель для нижнего навигацинного меню
     */

    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            User.search_requests_amount = 0;
            switch (menuItem.getItemId()){
                case R.id.allRequests:{
                    doFragmentTransaction(new AllRequestsFragment(), getString(R.string.allRequests));
                    menu.findItem(R.id.filterOptions).setVisible(true);
                    menu.findItem(R.id.clearSearchFilters).setVisible(false);
                    menu.findItem(R.id.smoothScrollToTop).setVisible(false);
                    break;
                }
                case R.id.myRequests:{
                    doFragmentTransaction(new MyRequestsFragment(), getString(R.string.myRequests));
                    menu.findItem(R.id.filterOptions).setVisible(true);
                    menu.findItem(R.id.clearSearchFilters).setVisible(false);
                    menu.findItem(R.id.smoothScrollToTop).setVisible(false);
                    break;
                }
                case R.id.map:{
                    actionBar.setElevation(0);
                    actionBar.setTitle(getString(R.string.map));
                    break;
                }
                case R.id.search:{
                    doFragmentTransaction(new SearchRequestsFragment(), getString(R.string.search));
                    menu.findItem(R.id.filterOptions).setVisible(false);
                    menu.findItem(R.id.smoothScrollToTop).setVisible(false);
                    break;
                }
                case R.id.settings:{
                    doFragmentTransaction(new SettingsFragment(), getString(R.string.settingsText));
                    menu.findItem(R.id.filterOptions).setVisible(false);
                    menu.findItem(R.id.clearSearchFilters).setVisible(false);
                    menu.findItem(R.id.smoothScrollToTop).setVisible(false);
                    break;
                }
            }
            return true;
        }
    };

    /**
     * Замена FrameLayout на Fragment
     * @param fragment Fragment который поменятся c FrameLayout
     * @param actionBarTitle Название странички
     */

    private void doFragmentTransaction(Fragment fragment, String actionBarTitle){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.main_container, fragment).commit();
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setTitle(actionBarTitle);
        actionBar.setElevation(0);
    }

    /**
     * Отчистить все данные с виджетов
     */

    private void clearAllWidgetsData() throws NullPointerException{
        SearchRequestsFragment.clearAllWidgetsData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        MainActivity.menu = menu;
        menu.findItem(R.id.clearSearchFilters).setVisible(false);
        menu.findItem(R.id.smoothScrollToTop).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.filterOptions:{
                BottomSheetDialogFragmentFilterEmployeeRequests bottomSheetDialog = new BottomSheetDialogFragmentFilterEmployeeRequests();
                bottomSheetDialog.show(getSupportFragmentManager(), getString(R.string.open_dialog));
            }

            case R.id.clearSearchFilters:{
                clearAllWidgetsData();
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setRequests(ArrayList<EmployeeRequest> requests) {
        ViewSearchRequestsFragment.setRequests(requests);
    }

    @Override
    public void setSqlParams(String p_sql_statement, String p_sql_statement_count_rows) {
        ViewSearchRequestsFragment.setSqlParams(p_sql_statement, p_sql_statement_count_rows);
    }
}
