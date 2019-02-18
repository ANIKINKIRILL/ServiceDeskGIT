package com.example.admin.oracletest.Models;


import android.util.Log;

import com.example.admin.oracletest.AsyncTaskArguments;
import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.GetDataFromKfuServer;

/**
 * Класс для работы с сервером КФУ
 */

public class ServerKFU {

    /**
     * ip адрес сервера КФУ
     */
    public static String ip = "portal-dis.kpfu.ru";

    /**
     * Авторизация пользователя
     *
     * @param login    Логин
     * @param password Пароль
     * @param callback Callback вызываемый после получения данных авторизации
     */
    public static void authenticateUser(String login, String password, Callback callback) {
        //Собираем url и параметры вместе
        String url = createUrl("test", "p_login=" + login,
                "p_password=" + password);
        //Создаем задачу, котрая будет выполняться на background
        GetDataFromKfuServer task = new GetDataFromKfuServer();
        //Параметры для задачи
        AsyncTaskArguments taskArguments = new AsyncTaskArguments();
        taskArguments.Data = url;
        taskArguments.Callback = callback;
        //Вызываем task
        task.execute(taskArguments);
    }

    /**
     * Собираем url
     *
     * @param procedureName имя процедуры
     * @param params        параметры для передачи в процедуру
     * @return возвращаемый url(Например:http://10.160.178.14/pls/mobile/test?p_login=login&p_password=password")
     */
    private static String createUrl(String procedureName, String... params) {
        //Собираем ip, имя процедуры вместе
        String url = String.format("https://%s/pls/mobile/%s", ip, procedureName);
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
