package com.example.admin.oracletest.ui.main;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.admin.oracletest.BaseActivity;
import com.example.admin.oracletest.R;
import com.example.admin.oracletest.models.EmployeeRequest;
import com.example.admin.oracletest.ui.main.all_requests.AllRequestsFragment;
import com.example.admin.oracletest.ui.main.my_requests.MyRequestsFragment;
import com.example.admin.oracletest.ui.main.search.OnViewSearchRequestsFragmentListener;
import com.example.admin.oracletest.ui.main.search.OnViewSearchRequestsFragmentSqlParams;
import com.example.admin.oracletest.ui.main.search.SearchFragment;
import com.example.admin.oracletest.ui.main.search.ViewSearchFragment;
import com.example.admin.oracletest.ui.main.settings.SettingsFragment;

import java.util.List;

/**
 * Activity with All Fragments (All Requests, My Requests, Map, Search, Settings)
 */

public class MainActivity extends BaseActivity implements OnViewSearchRequestsFragmentListener, OnViewSearchRequestsFragmentSqlParams {

    private static final String TAG = "MainActivity";

    // Виджеты
    BottomNavigationView bottomNavigationView;
    FrameLayout mainContainer;


    // Переменные
    public static ActionBar actionBar;
    public static Menu menu;
    public static int createCounter = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitymain);
        initActionBar();
        init();
        if(AllRequestsFragment.status_id == 1 && createCounter == 0) {
            setLaunchFragment();
        }
    }

    /**
     * Init ui
     */

    private void init(){
        bottomNavigationView = findViewById(R.id.main_bottom_nav_view);
        mainContainer = findViewById(R.id.main_container);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    /**
     * Init ActionBar
     */

    private void initActionBar(){
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.kfuDefaultColor)));
        actionBar.setElevation(0);
    }

    /**
     * Set launch fragment
     */

    private void setLaunchFragment(){
        doFragmentTransaction(new AllRequestsFragment(),  getString(R.string.allRequests));
    }

    /**
     * Listener for bottom menu
     */

    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
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
                    break;
                }
                case R.id.search:{
                    doFragmentTransaction(new SearchFragment(), getString(R.string.search));
                    menu.findItem(R.id.clearSearchFilters).setVisible(true);
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
     * Replace FrameLayout with Fragment
     * @param fragment Fragment which will be replaced with FrameLayout
     * @param actionBarTitle fragment title
     */

    private void doFragmentTransaction(Fragment fragment, String actionBarTitle){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment).commit();
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setTitle(actionBarTitle);
        actionBar.setElevation(0);
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
                SearchFragment.clearAllWidgetsData();
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Methods For Communication between {@link com.example.admin.oracletest.ui.main.search.SearchFragment} and
     * {@link com.example.admin.oracletest.ui.main.search.ViewSearchFragment}
     */

    @Override
    public void setRequests(List<EmployeeRequest> requests) {
        ViewSearchFragment.setRequests(requests);
    }

    @Override
    public void setSqlParams(String p_sql_statement, String p_sql_statement_count_rows) {
        ViewSearchFragment.setSqlParams(p_sql_statement, p_sql_statement_count_rows);
    }
}
