package com.example.admin.oracletest;

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

    @GET("servicedesk_mobile_test.get_employee_requests")
    Call<Object> get_requests(
            @Query("p_emp_id") String u_id,
            @Query("p_page_number") int page_number
    );

}
