package com.test.admin.servicedesk.ui.auth;

/**
 * This class for keep track of the user authentication status
 * Helps to update ui automatically, because we`re gonna make this observable
 * @param   <T>     object that`s gonna be used. In {@link AuthViewModel}
 * it`s gonna be {@link com.test.admin.servicedesk.models.User}
 */

public class AuthResource<T> {

    public String msg;
    public   T data ;
    public  Status status;

    public AuthResource(String msg, T data, Status status) {
        this.msg = msg;
        this.data = data;
        this.status = status;
    }

    public static <T> AuthResource<T> loading(T data){
        return new AuthResource<>(null, data, Status.LOADING);
    }

    public static <T> AuthResource<T> error(String msg, T data){
        return new AuthResource<>(msg, data, Status.ERROR);
    }

    public static <T> AuthResource<T> authenticated(T data){
        return new AuthResource<>(null, data, Status.AUTHENTICATED);
    }

    public static <T> AuthResource<T> logout(){
        return new AuthResource<>(null, null, Status.NOT_AUTHENTICATED);
    }

    public enum Status {LOADING, ERROR, AUTHENTICATED, NOT_AUTHENTICATED};

}
