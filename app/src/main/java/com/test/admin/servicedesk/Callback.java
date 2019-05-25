package com.test.admin.servicedesk;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Callback, который вызывается после получения данных с Сервера
 */

public interface Callback {
    public void execute(Object data);
}
