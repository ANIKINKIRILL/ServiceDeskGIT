package com.example.admin.oracletest;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Callback, который вызывается после получения данных с Сервера
 */

public interface Callback {
    public void execute(Object data);
}
