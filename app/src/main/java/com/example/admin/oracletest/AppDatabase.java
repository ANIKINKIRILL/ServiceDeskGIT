package com.example.admin.oracletest;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.admin.oracletest.Dao.EmployeeRequestDao;
import com.example.admin.oracletest.Models.EmployeeRequest;

@Database(entities = {EmployeeRequest.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    /*
            SINGLETON PATTERN
     */

    public static AppDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    context.getResources().getString(R.string.local_db_name)
            ).build();
        }
        return instance;
    }

    /*      **********************************************
     *      Data Access Object for 'EmployeeRequest' table
     *      **********************************************
     */

    public abstract EmployeeRequestDao getEmployeeRequestDao();

}
