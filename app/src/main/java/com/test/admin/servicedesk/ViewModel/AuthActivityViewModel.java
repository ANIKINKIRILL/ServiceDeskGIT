package com.test.admin.servicedesk.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.test.admin.servicedesk.Activity.AuthActivity;
import com.test.admin.servicedesk.Callback;
import com.test.admin.servicedesk.GetDataFromKfuServer;
import com.test.admin.servicedesk.Models.User;
import com.test.admin.servicedesk.Repository.Repository;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * ViewModel для {@link AuthActivity}
 * Здесь представлена вся основная бизнес логика для AuthActivity.
 * Вход пользоватателя в систему ServiceDesk
 */

public class AuthActivityViewModel extends ViewModel {

    private static final String TAG = "AuthActivityViewModel";
    private static Callback externalCallback;

    /**
     * Авторизация пользователя
     * @param login              Логин пользователья
     * @param password           Пароль пользователья
     * @param callback           External callback с {@link AuthActivity}
     */

    public void authenticateUser(Context context, String login, String password, Callback callback){
        Log.d(TAG, "authenticateUser: called");
        User.login = login;
        User.password = password;
        externalCallback = callback;
        Repository.getInstance(context).authenticateUser(login, password, mUserAuthCallback);
    }

    /**
     * Callback, который вернеться после получения JSON
     * {@link GetDataFromKfuServer}
     * Если пользователь успешно авторизован, то отправляем в callback
     * на{@link AuthActivity} true,
     * иначе false
     */

    private static Callback mUserAuthCallback = new Callback() {
        @Override
        public void execute(Object data) {
            try {
                JSONObject jsonObject = new JSONObject(data.toString());
                boolean successful = jsonObject.getBoolean("successful");
                if(successful){
                    User.user_id = jsonObject.getInt("user_id");
                    User.firstname = jsonObject.getString("firstname");
                    User.middlename = jsonObject.getString("lastname");
                    User.lastname = jsonObject.getString("middlename");
                    Log.d(TAG, "execute: " + User.login + " " + User.password);
                    User.isAuthorized = true;
                    User.saveInformation();
                    externalCallback.execute(true);
                }else{
                    externalCallback.execute(false);
                }
            } catch (JSONException e) {
                Log.d(TAG, "execute: " + e.getMessage());
            }
        }
    };

}
