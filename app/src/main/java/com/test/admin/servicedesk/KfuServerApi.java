package com.test.admin.servicedesk;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 *
 * Интерфес для хранения методов, которые
 * используются для получения JSON с сервера КФУ
 *
 */


public interface KfuServerApi {

    /**
     * Функция для авторизации пользователя в системе
     * ServiceDesk
     * @param p_login       логин пользователя
     * @param p_pass        пароль пользователя
     */

    @GET("portal_pg_mobile.authentication")
    Call<Object> authenticateUser(
            @Query("p_login") String p_login,
            @Query("p_pass") String p_pass
    );

    /**
     * Получить заявки исполнителя
     *
     * @param u_id      id исполнителя
     * @param page_number номер страницы с заявками
     */

    @GET("servicedesk_mobile.get_employee_requests2")
    Call<Object> get_requests(
            @Query("u_id") String u_id,
            @Query("page_number") int page_number
    );

}
