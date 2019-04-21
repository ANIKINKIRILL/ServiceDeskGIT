package com.example.admin.oracletest.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Класс Заявка. Делаем аннатацию для того чтобы
 * создать таблицу 'REQUEST', которая будет
 * на local/device database. Room Persistence Library.
 */

@Entity(tableName = "REQUEST")
public class EmployeeRequest {

    @PrimaryKey
    private int id;     // id заявки в табилце TECH_CENTER$DB.REQUEST
    private String image;       // путь к картике для статуса заявки
    private String request_date;        // дата регистрации заявки
    private String date_of_realization;     // дедлайн
    private String declarant_fio;       // ФИО заявителя
    private String post;        // должоость заявителя
    private String building_kfu_name;       // адрес КФУ
    private String room_number;     // комната
    private String descr;       // описание заявки
    private String status_name;     // статус заявки
    private String color;       // цвет заявки
    private String phone;       // телефон заявителя
    private String info;        // иформация о заявки
    private int cod;        // код заявки

    @Ignore
    public EmployeeRequest(){}

    public EmployeeRequest(int id, String image, String request_date, String date_of_realization,
                           String declarant_fio, String post, String building_kfu_name,
                           String room_number, String descr, String status_name, String color, String phone,
                           int cod, String info) {
        this.id = id;
        this.image = image;
        this.request_date = request_date;
        this.date_of_realization = date_of_realization;
        this.declarant_fio = declarant_fio;
        this.post = post;
        this.building_kfu_name = building_kfu_name;
        this.room_number = room_number;
        this.descr = descr;
        this.status_name = status_name;
        this.color = color;
        this.phone = phone;
        this.cod = cod;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRequest_date() {
        return request_date;
    }

    public void setRequest_date(String request_date) {
        this.request_date = request_date;
    }

    public String getDate_of_realization() {
        return date_of_realization;
    }

    public void setDate_of_realization(String date_of_realization) {
        this.date_of_realization = date_of_realization;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getDeclarant_fio() {
        return declarant_fio;
    }

    public void setDeclarant_fio(String declarant_fio) {
        this.declarant_fio = declarant_fio;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getBuilding_kfu_name() {
        return building_kfu_name;
    }

    public void setBuilding_kfu_name(String building_kfu_name) {
        this.building_kfu_name = building_kfu_name;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
