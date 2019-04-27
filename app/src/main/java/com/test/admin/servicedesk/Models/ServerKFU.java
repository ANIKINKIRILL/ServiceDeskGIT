package com.test.admin.servicedesk.Models;


import android.util.Log;

import com.test.admin.servicedesk.Callback;
import com.test.admin.servicedesk.Data.AsyncTaskArguments;
import com.test.admin.servicedesk.GetDataFromKfuServer;

/**
 * Класс для работы с сервером КФУ
 */

public class ServerKFU {

    // Лог
    private static final String TAG = "ServerKFU";

    // Переменные
    private static String ip = "portal-dis.kpfu.ru"; //     ip адрес сервера КФУ

    /**
     * Авторизация пользователя
     *
     * @param p_login     Логин пользователья
     * @param p_password  Пароль пользователья
     * @param callback    {@link Callback} вызовиться когда будут получены данные с {@link GetDataFromKfuServer}.
     *                    Этот интерфес нужен, чтобы разпарсить JSON, который веренеться после асинхронного
     *                    получения. Зашел ли пользователь удачно или авторизация окончилась ошибкой.
     */

    public static void authenticateUser(String p_login, String p_password, Callback callback){
        // создаем адрес обращения к серверу
        String url = createUrl("PORTAL_PG_MOBILE", "authentication", "p_login=" + p_login, "p_pass=" + p_password);
        Log.d(TAG, "authenticateUser: " + url);
        /**
         *  создаем параметры для асинхронного класса
         *  {@link GetDataFromKfuServer}
         */
        AsyncTaskArguments arguments = new AsyncTaskArguments(url, callback);
        GetDataFromKfuServer server = new GetDataFromKfuServer();
        server.execute(arguments);
    }

    /**
     * Получить заявки исполнителя
     *
     * @param u_id      id исполнителя
     * @param callback  callback, который вернеться полсе получения заявок на исполнителя
     */

    public static void get_requests(String u_id, Callback callback){
        // создаем аддрес обращения к серверу
        String url = createUrl("SERVICEDESK_MOBILE", "get_employee_requests2", "u_id=" + u_id);
        /**
         *  создаем параметры для асинхронного класса
         *  {@link GetDataFromKfuServer}
         */
        AsyncTaskArguments arguments = new AsyncTaskArguments(url, callback);
        GetDataFromKfuServer server = new GetDataFromKfuServer();
        server.execute(arguments);
    }

    /**
     * Собираем url
     *
     * @param procedureName имя процедуры
     * @param packageName имя пакета
     * @param params        параметры для передачи в процедуру
     * @return возвращаемый url(Например:http://10.160.178.14/pls/mobile/test?p_login=login&p_password=password")
     */
    private static String createUrl(String packageName, String procedureName, String... params) {
        //Собираем ip, имя пакета и имя процедуры вместе
        String url = String.format("https://%s/e-ksu/%s.%s", ip, packageName, procedureName);
        //Если есть параметры, начинаем записывать через знак ?
        if (params.length > 0)
            url += "?";
        //Проходим по каждому параметру
        for (int i = 0; i < params.length; i++) {
            //Если это первый параметр, то перед ним не ставим &
            if (i != 0)
                //Иначе ставим
                url += "&";
            //Добавляем параметр к строке
            url += params[i];
        }
        return url;
    }
}
