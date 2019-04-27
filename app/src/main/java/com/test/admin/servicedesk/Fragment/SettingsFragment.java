package com.test.admin.servicedesk.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.test.admin.servicedesk.Activity.AuthActivity;
import com.test.admin.servicedesk.Models.User;
import com.test.admin.servicedesk.R;

/**
 * Фрагмент с настройками
 */

public class SettingsFragment extends Fragment implements View.OnClickListener{

    // Виджеты
    private Button exitButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settingsfragment, container, false);
        init(view);
        return view;
    }

    /**
     * Инициализация виджетов
     * @param view      view
     */

    private void init(View view){
        exitButton = view.findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exitButton:{
                User.exit();
                Intent intent = new Intent(getContext(), AuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            }
        }
    }
}
