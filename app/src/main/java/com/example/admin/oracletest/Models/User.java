package com.example.admin.oracletest.Models;

import android.annotation.SuppressLint;

import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.Settings;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Класс пользователя
 */

public class User {

    // Logs
    private static final String TAG = "User";

    // vars
    public static boolean isAuthorized; // Авторизован ли пользователь
    public static String login;
    public static String password;
    public static int user_id;
    public static String firstname;
    public static String lastname;
    public static String middlename;


    /**
        * Авторизация пользователя
        *
        * @param login              Логин пользователья
        * @param password           Пароль пользователья
     */

    public static void authenticate(String login, String password, Callback callback){
        User.login = login;
        User.password = password;
        // Создаем observable
        Observable.fromCallable(() -> {
            ServerKFU.authenticateUser(login, password, callback);
            return null;
            // Весь процесс просиходит на Background Thread
            // Вывод данных будет на UI Thread
            // Подписываем observer, чтобы на любое изменение
            // UI был обновленным
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<Object>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Object o) {

                }

                @Override
                public void onError(Throwable e) {
                    e.getMessage();
                }

                @Override
                public void onComplete() {

                }
            });
    }

    /**
     * Получить заявки исполнителя
     * @param callback      Callback, который вернется после получения заявок на исполнителя
     * @param u_id          id исполнителя
     */

    @SuppressLint("CheckResult")
    public static void get_requests(String u_id, Callback callback){
        // Создаем observable со списком запросов на исполнителя
        Observable<List<EmployeeRequest>> listEmployeeRequestsObservable = Observable.fromCallable(() -> ServerKFU.get_requests(u_id))
                // сам процесс получения запросов с сервера произходид
                // на background thread
                .subscribeOn(Schedulers.io())
                // получаем ответ на main thread
                .observeOn(AndroidSchedulers.mainThread());
        // при помощи callback передаем список с запросами на конкретного
        // исполнителя обратно
        listEmployeeRequestsObservable.subscribe(callback::execute);
    }

    /**
     * Сохранение логина пароля пользователя в настройках
     */

    public static void saveInformation(){
        Settings.setUserLogin(login);
        Settings.setUserPassword(password);
        Settings.setUserId(Integer.toString(user_id));
        Settings.setUserFirstName(firstname);
        Settings.setUserMiddleName(middlename);
        Settings.setUserLastName(lastname);
    }

    /**
     * Выход пользователя
     */

    public static void exit(){
        isAuthorized = false;
        Settings.setUserFirstName("");
        Settings.setUserMiddleName("");
        Settings.setUserLastName("");
        Settings.setUserId("");
        Settings.setUserLogin("");
        Settings.setUserPassword("");
    }

}
