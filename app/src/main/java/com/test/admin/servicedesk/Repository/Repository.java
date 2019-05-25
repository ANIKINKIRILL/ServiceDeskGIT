package com.test.admin.servicedesk.Repository;

import android.content.Context;
import android.util.Log;

import com.test.admin.servicedesk.AppDatabase;
import com.test.admin.servicedesk.Callback;
import com.test.admin.servicedesk.Models.ServerKFU;
import com.test.admin.servicedesk.ViewModel.AuthActivityViewModel;

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
     * @param callback           Callback с {@link AuthActivityViewModel}
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

    public void get_requests(String u_id, int page_number, int status_id, Callback callback){
        Log.d(TAG, "get_requests: called");
        ServerKFU.get_requests(u_id, page_number, status_id, callback);
    }

    /**
     * Получить текущие заявки
     * @param page_number       номер страницы с заявками
     * @param status_id         статус заявки (новая, выполненная и т.д)
     * @param callback          callback для парсинга json после получения текущих заявок c сервера
     * @return                  LiveData из списка с текущими заявками
     */

    public void get_current_requests(int page_number, int status_id, Callback callback){
        ServerKFU.get_current_requests(page_number, status_id, callback);
    }

    /**
     * Получить результаты поиска
     * @param p_sql_statement   SQL запрос на сервер
     * @param p_sql_statement_count_rows    SQL запрос на сервер для подсчета найденных строк
     * @param p_page_number     номер страницы с заявками
     * @param callback          callback с {@link com.test.admin.servicedesk.ViewModel.SearchRequestsFragmentViewModel}
     */

    public void search_request(String p_sql_statement, String p_sql_statement_count_rows, int p_page_number, Callback callback){
        ServerKFU.search_request(p_sql_statement, p_sql_statement_count_rows, p_page_number, callback);
    }

}
