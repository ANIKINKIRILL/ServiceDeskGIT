package com.test.admin.servicedesk.Models;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.test.admin.servicedesk.AppDatabase;
import com.test.admin.servicedesk.Callback;
import com.test.admin.servicedesk.Data.AsyncTaskArguments;
import com.test.admin.servicedesk.DirectoryServiceDesk;
import com.test.admin.servicedesk.GetDataFromKfuServer;
import com.test.admin.servicedesk.KfuServerApi;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Класс для работы с сервером КФУ
 */

public class ServerKFU {

    // Лог
    private static final String TAG = "ServerKFU";

    // Переменные
    private static String ip = "portal-dis.kpfu.ru"; //     ip адрес сервера КФУ
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://" + ip + "/e-ksu/")
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
            .build();
    private static KfuServerApi kfuServerApi = retrofit.create(KfuServerApi.class);

    /**
     * Авторизация пользователя
     *
     * @param p_login     Логин пользователья
     * @param p_password  Пароль пользователья
     * @param callback    {@link Callback} вызовиться когда будут получены данные с {@link GetDataFromKfuServer}.
     *                    Этот интерфес нужен, чтобы разпарсить JSON, который веренеться после асинхронного
     *                    получения. Зашел ли пользователь удачно или авторизация окончилась ошибкой.
     */

    public static void authenticateUser(String p_login, String p_password, Callback callback){
        // Функция для авторизации пользователя в системе ServiceDesk
        Call<Object> call = kfuServerApi.authenticateUser(p_login, p_password);
        // Сдлеать запрос по url на Background Thread, но получить данные на Main Thread
        call.enqueue(new retrofit2.Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                // Если запрос с ошибкой (404, 501, 505...)
                if(!response.isSuccessful()){
                    Toast.makeText(DirectoryServiceDesk.GetAppContext(), "Ошибка " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                // Получить сам JSON
                Object json = response.body();
                // Отправить его на AuthActivityViewModel callback
                callback.execute(json);
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                // Если запрос не удался
                Toast.makeText(DirectoryServiceDesk.GetAppContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Получить заявки исполнителя
     *
     * @param u_id      id исполнителя
     * @param callback  callback, который вернеться полсе получения заявок на исполнителя
     * @param page_number номер страницы с заявками
     */

    public static void get_requests(String u_id, int page_number, Callback callback){
        Log.d(TAG, "get_requests: called");
        Call<Object> call = kfuServerApi.get_requests(u_id, page_number);
        Log.d(TAG, "get_requests: " + call.request().url().toString());
        call.enqueue(new retrofit2.Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(DirectoryServiceDesk.GetAppContext(), "Ошибка " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Object json = response.body();
                callback.execute(json);
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(DirectoryServiceDesk.GetAppContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Собираем url
     *
     * @param procedureName имя процедуры
     * @param packageName имя пакета
     * @param params        параметры для передачи в процедуру
     * @return возвращаемый url(Например:http://10.160.178.14/pls/mobile/test?p_login=login&p_password=password")
     */
    private static String createUrl(String packageName, String procedureName, String... params) {
        //Собираем ip, имя пакета и имя процедуры вместе
        String url = String.format("https://%s/e-ksu/%s.%s", ip, packageName, procedureName);
        //Если есть параметры, начинаем записывать через знак ?
        if (params.length > 0)
            url += "?";
        //Проходим по каждому параметру
        for (int i = 0; i < params.length; i++) {
            //Если это первый параметр, то перед ним не ставим &
            if (i != 0)
                //Иначе ставим
                url += "&";
            //Добавляем параметр к строке
            url += params[i];
        }
        return url;
    }
}
