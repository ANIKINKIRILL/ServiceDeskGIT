package com.example.admin.oracletest.Activity;

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
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.admin.oracletest.Fragment.AllRequestsFragment;
import com.example.admin.oracletest.Fragment.MyRequestsFragment;
import com.example.admin.oracletest.Fragment.SettingsFragment;
import com.example.admin.oracletest.R;
import com.example.admin.oracletest.Settings;

/**
 * Главное Активити
 */

public class MainActivity extends AppCompatActivity {

    // widgets
    BottomNavigationView bottomNavigationView;
    FrameLayout mainContainer;

    // vars
    ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitymain);
        init();
        initActionBar();
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
        doFragmentTransaction(new AllRequestsFragment(), getString(R.string.allRequests));
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
                    doFragmentTransaction(new MyRequestsFragment(), getString(R.string.myRequests));
                    break;
                }
                case R.id.map:{
                    actionBar.setElevation(0);
                    actionBar.setTitle(getString(R.string.map));
                    break;
                }
                case R.id.search:{
                    actionBar.setElevation(0);
                    actionBar.setTitle(getString(R.string.search));
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
        actionBar.setTitle(actionBarTitle);
        actionBar.setElevation(0);
    }

}
