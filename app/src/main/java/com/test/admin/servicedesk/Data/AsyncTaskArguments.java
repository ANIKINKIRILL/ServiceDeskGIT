package com.test.admin.servicedesk.Data;

import com.test.admin.servicedesk.Callback;

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
    public com.test.admin.servicedesk.Callback Callback;

    public AsyncTaskArguments() {

    }

    public AsyncTaskArguments(Object data, Callback callback) {
        Data = data;
        Callback = callback;
    }
}
