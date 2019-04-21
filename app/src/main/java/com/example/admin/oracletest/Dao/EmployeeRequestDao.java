package com.example.admin.oracletest.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import com.example.admin.oracletest.Models.EmployeeRequest;

/**
 * Data Access Object для 'EmployeeRequest' таблицы
 * Этот интерфес хранит функции, методы CRUD
 * которые будет вносить изменения в SQLite на
 * девайсе.
 */

@Dao
public interface EmployeeRequestDao {

    /**
     * Добавить заявку
     * @param employeeRequest       Заявка, котроая будет добавленна в бд
     */

    @Insert
    void addEmployeeRequest(EmployeeRequest employeeRequest);

}
