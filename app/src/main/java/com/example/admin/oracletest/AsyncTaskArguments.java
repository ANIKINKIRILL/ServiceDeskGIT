package com.example.admin.oracletest;

public class AsyncTaskArguments {
    /**
     * Содержит определенные данные
     */
    public Object Data;
    /**
     * Callback функция, которая исполнится, после получения данных
     */
    public Callback Callback;

    public AsyncTaskArguments() {

    }

    public AsyncTaskArguments(Object data, Callback callback) {
        Data = data;
        Callback = callback;
    }
}
