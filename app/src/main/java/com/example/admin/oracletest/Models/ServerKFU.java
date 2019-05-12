package com.example.admin.oracletest.Models;

import android.util.Log;
import android.widget.Toast;

import com.example.admin.oracletest.Data.AsyncTaskArguments;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.admin.oracletest.Callback;
import com.example.admin.oracletest.DirectoryServiceDesk;
import com.example.admin.oracletest.GetDataFromKfuServer;
import com.example.admin.oracletest.KfuServerApi;

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
        Log.d(TAG, "authenticateUser: called");
        // Функция для авторизации пользователя в системе ServiceDesk
        Call<Object> call = kfuServerApi.authenticateUser(p_login, p_password);
        Log.d(TAG, "authenticateUser: " + call.request().url().toString());
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

    public static void get_requests(String u_id, int page_number, int status_id, Callback callback){
        String url = createUrl("servicedesk_mobile_test",
                "get_employee_requests",
                "p_emp_id=" + u_id,
                "p_page_number=" + page_number,
                "p_status_id=" + status_id
        );
        AsyncTaskArguments arguments = new AsyncTaskArguments(url, callback);
        GetDataFromKfuServer backgroundTask = new GetDataFromKfuServer();
        backgroundTask.execute(arguments);
    }

    /**
     * Получить текущие заявки
     * @param page_number       номер страницы с заявками
     * @param status_id         статус заявки (новая, выполненная и т.д)
     * @param callback          callback для парсинга json после получения текущих заявок c сервера
     * @return                  LiveData из списка с текущими заявками
     */

    public static void get_current_requests(int page_number, int status_id, Callback callback){
        String url = createUrl("servicedesk_mobile_test",
                "get_current_requests",
                "p_page_number=" + page_number,
                "p_status_id=" + status_id
        );
        AsyncTaskArguments arguments = new AsyncTaskArguments(url, callback);
        GetDataFromKfuServer backgroundTask = new GetDataFromKfuServer();
        backgroundTask.execute(arguments);
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
