package com.example.admin.oracletest.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.util.Log;

import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.Repository.Repository;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *  ViewModel для {@link com.example.admin.oracletest.Fragment.SearchRequestsFragment}
 */

public class SearchRequestsFragmentViewModel extends ViewModel {

    private static final String TAG = "SearchRequestsFragVM";

    private static Callback externalCallback;

    /**
     * Получить результаты поиска
     * @param p_sql_statement   SQL запрос на сервер
     * @param callback          callback с {@link com.example.admin.oracletest.Fragment.SearchRequestsFragment}
     */

    public void search_request(String p_sql_statement, Callback callback, Context context){
        externalCallback = callback;
        Repository.getInstance(context).search_request(p_sql_statement, mSearchRequestCallback);
    }

    /**
     * Callback, который вернётся после получения данных с сервера КФУ
     */

    private static Callback mSearchRequestCallback = new Callback() {
        @Override
        public void execute(Object data) {
            externalCallback.execute(data.toString());
        }
    };

}
