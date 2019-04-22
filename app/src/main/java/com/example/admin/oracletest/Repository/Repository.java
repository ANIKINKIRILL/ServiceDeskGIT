package com.example.admin.oracletest.Repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.admin.oracletest.AppDatabase;
import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.Models.EmployeeRequest;
import com.example.admin.oracletest.Models.ServerKFU;

import java.util.List;

/**
 * Абстрактный уровень между бизнес логикой и
 * логикой базы данных. Репозиторий дает
 * возможность разграничить SQLite и Server
 */

public class Repository {

    private static Repository instance;
    private static AppDatabase appDatabase;

    public static Repository getInstance(Context context) {
        if(instance == null){
            instance = new Repository();
        }
        appDatabase = AppDatabase.getInstance(context);
        return instance;
    }

    /*
            ***********************************************
                    Обращение к серверу за JSON
            ***********************************************
     */

    /**
     * Авторизация пользователя
     * @param login              Логин пользователья
     * @param password           Пароль пользователья
     * @param callback           Callback с {@link com.example.admin.oracletest.ViewModel.AuthActivityViewModel}
     */

    public void authenticateUser(String login, String password, Callback callback){
        ServerKFU.authenticateUser(login, password, callback);
    }

    /**
     * Получить заявки исполнителя
     * @param callback Callback, который вернется после получения заявок на исполнителя
     * @param u_id     id исполнителя
     */

    public void get_requests(String u_id, Callback callback){
        ServerKFU.get_requests(u_id, callback);
    }

}
