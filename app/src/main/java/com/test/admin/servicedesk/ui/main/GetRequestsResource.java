package com.test.admin.servicedesk.ui.main;

import android.util.Log;

/**
 * This class for keep track of the loading requests status
 * Helps to update ui automatically, because we`re gonna make this observable
 * @param   <T>     object that`s gonna be used. In {@link MainActivity} fragments
 * it`s gonna be {@link com.test.admin.servicedesk.models.RequestsPage}
 */

public class GetRequestsResource<T> {

    private static final String TAG = "GetRequestsResource";

    public String msg;
    public   T data ;
    public  Status status;

    public GetRequestsResource(String msg, T data, Status status) {
        this.msg = msg;
        this.data = data;
        this.status = status;
    }

    public static <T> GetRequestsResource<T> loading(T data){
        Log.d(TAG, "loading: called");
        return new GetRequestsResource<>(null, data, Status.LOADING);
    }

    public static <T> GetRequestsResource<T> error(String msg, T data){
        return new GetRequestsResource<>(msg, data, Status.ERROR);
    }

    public static <T> GetRequestsResource<T> success(T data){
        return new GetRequestsResource<>(null, data, Status.SUCCESS);
    }

    public enum Status {LOADING, ERROR, SUCCESS};

}
