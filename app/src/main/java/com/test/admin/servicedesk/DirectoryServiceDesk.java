package com.test.admin.servicedesk;

import android.app.Application;
import android.content.Context;

/**
 * Класс Приложения
 */

public class DirectoryServiceDesk extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        DirectoryServiceDesk.context = getApplicationContext();
    }

    public static Context GetAppContext(){
        return context;
    }

}
