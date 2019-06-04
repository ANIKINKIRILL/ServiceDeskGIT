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
import com.example.admin.oracletest.ui.main.all_requests.AllRequestsFragment;
import com.example.admin.oracletest.ui.main.settings.SettingsFragment;

import java.util.Base64;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Activity with All Fragments (All Requests, My Requests, Map, Search, Settings)
 */

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    // Виджеты
    BottomNavigationView bottomNavigationView;
    FrameLayout mainContainer;


    // Переменные
    public static ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitymain);
        initActionBar();
        init();
        setLaunchFragment();
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
        doFragmentTransaction(new AllRequestsFragment(),  getString(R.string.allRequests));
    }

    /**
     * Слушатель для нижнего навигацинного меню
     */

    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.allRequests:{
                    doFragmentTransaction(new AllRequestsFragment(), getString(R.string.allRequests));
                    break;
                }
                case R.id.myRequests:{
                    break;
                }
                case R.id.map:{
                    break;
                }
                case R.id.search:{
                    break;
                }
                case R.id.settings:{
                    doFragmentTransaction(new SettingsFragment(), getString(R.string.settingsText));
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
        fragmentTransaction.replace(R.id.main_container, fragment).commit();
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setTitle(actionBarTitle);
        actionBar.setElevation(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
