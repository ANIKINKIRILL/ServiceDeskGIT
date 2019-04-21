package com.example.admin.oracletest.Repository;

import android.content.Context;

import com.example.admin.oracletest.AppDatabase;

/**
 * Абстрактный уровень между бизнес логикой и
 * логикой базы данных. Репозиторий дает
 * возможность разграничить SQLite и Server
 */

public class Repository {

    private static Repository instance;
    private static AppDatabase appDatabase;

    public static Repository getInstance(Context context) {
        if(instance == null){
            instance = new Repository();
        }
        appDatabase = AppDatabase.getInstance(context);
        return instance;
    }
}
