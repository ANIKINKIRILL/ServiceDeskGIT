package com.test.admin.servicedesk.Repository;

import android.content.Context;
import android.util.Log;

import com.test.admin.servicedesk.AppDatabase;
import com.test.admin.servicedesk.Callback;
import com.test.admin.servicedesk.Models.ServerKFU;

/**
 * Абстрактный уровень между бизнес логикой и
 * логикой базы данных. Репозиторий дает
 * возможность разграничить SQLite и Server
 */

public class Repository {

    private static final String TAG = "Repository";

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
     * @param callback           Callback с {@link com.test.admin.servicedesk.ViewModel.AuthActivityViewModel}
     */

    public void authenticateUser(String login, String password, Callback callback){
        Log.d(TAG, "authenticateUser: called");
        ServerKFU.authenticateUser(login, password, callback);
    }

    /**
     * Получить заявки исполнителя
     * @param callback Callback, который вернется после получения заявок на исполнителя
     * @param u_id     id исполнителя
     * @param page_number номер страницы с заявками
     */

    public void get_requests(String u_id, int page_number, Callback callback){
        Log.d(TAG, "get_requests: called");
        ServerKFU.get_requests(u_id, page_number, callback);
    }

}
