package com.example.admin.oracletest.Models;


import com.example.admin.oracletest.Data.AsyncTaskArguments;
import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.GetDataFromKfuServer;

/**
 * Класс для работы с сервером КФУ
 */

public class ServerKFU {

    // Logs
    private static final String TAG = "ServerKFU";

    /**
     * ip адрес сервера КФУ
     */
    public static String ip = "portal-dis.kpfu.ru";


    /**
     * Авторизация пользователя
     *
     * @param p_login              Логин пользователья
     * @param p_password           Пароль пользователья
     * @param callback           Функция, которая вызывается после получения данных
     */

    public static void authenticateUser(String p_login, String p_password, Callback callback) {
        // создаем аддрес обращения к серверу
        String url = createUrl("PORTAL_PG_MOBILE", "authentication", "p_login=" + p_login, "p_pass=" + p_password);
        // создаем задачу
        GetDataFromKfuServer server = new GetDataFromKfuServer();
        // аргументы для задачи
        AsyncTaskArguments asyncTaskArguments = new AsyncTaskArguments();
        asyncTaskArguments.Data = url;
        asyncTaskArguments.Callback = callback;
        server.execute(asyncTaskArguments);
    }

    /**
     * Получить заявки исполнителя
     * @param callback      Callback, который вернется после получения заявок на исполнителя
     * @param u_id          id исполнителя
     */

    public static void get_requests(String u_id, Callback callback){
        // создаем аддрес обращения к серверу
        String url = createUrl("SERVICEDESK_MOBILE", "get_employee_requests", "u_id=" + u_id);
        // создаем задачу
        GetDataFromKfuServer server = new GetDataFromKfuServer();
        // аргументы для задачи
        AsyncTaskArguments asyncTaskArguments = new AsyncTaskArguments();
        asyncTaskArguments.Data = url;
        asyncTaskArguments.Callback = callback;
        server.execute(asyncTaskArguments);
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
