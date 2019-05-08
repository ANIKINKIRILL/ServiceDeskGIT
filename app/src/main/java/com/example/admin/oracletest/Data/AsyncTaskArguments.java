package com.example.admin.oracletest.Data;

import com.example.admin.oracletest.Callback;

/**
 * Параметры передаваемые в GetDataFromKfuServer
 */

public class AsyncTaskArguments {
    /**
     * Содержит определенные данные
     */
    public Object Data;
    /**
     * Callback функция, которая исполнится, после получения данных
     */
    public com.example.admin.oracletest.Callback Callback;

    public AsyncTaskArguments() {

    }

    public AsyncTaskArguments(Object data, Callback callback) {
        Data = data;
        Callback = callback;
    }
}
