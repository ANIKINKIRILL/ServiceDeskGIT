package com.example.admin.oracletest.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeRequest {

    @SerializedName("id")
    @Expose
    private int id;     // id заявки в табилце TECH_CENTER$DB.REQUEST
    @SerializedName("emp_fio")
    @Expose
    private String emp_fio;     // ФИО исполнителя
    @SerializedName("request_date")
    @Expose
    private String request_date;        // дата регистрации заявки
    @SerializedName("phone")
    @Expose
    private String phone;       // телефон заявителя
    @SerializedName("declarant_fio")
    @Expose
    private String declarant_fio;       // ФИО заявителя
    @SerializedName("info")
    @Expose
    private String info;        // иформация о заявки
    @SerializedName("request_priority")
    @Expose
    private int request_priority;
    @SerializedName("date_of_realization")
    @Expose
    private String date_of_realization;     // дедлайн
    @SerializedName("status")
    @Expose
    private RequestStatus status;
    @SerializedName("reason")
    @Expose
    private String reason;
    @SerializedName("cod")
    @Expose
    private int cod;        // код заявки
    @SerializedName("type")
    @Expose
    private RequestType type;
    @SerializedName("post")
    @Expose
    private String post;        // должоость заявителя
    @SerializedName("date_of_reg")
    @Expose
    private String date_of_reg;
    @SerializedName("reg_user")
    @Expose
    private int reg_user;
    @SerializedName("zaavitel")
    @Expose
    private int zaavitel;
    @SerializedName("building_kfu")
    @Expose
    private RequestBuildingKfu building_kfu;       // адрес КФУ
    @SerializedName("room_number")
    @Expose
    private String room_number;     // комната
    @SerializedName("image")
    @Expose
    private String image;       // путь к картике для статуса заявки
    @SerializedName("descr")
    @Expose
    private String descr;       // описание заявки

    public static final String NEW = "новая";
    public static final String DONE = "выполнена";
    public static final String IN_PROGRESS = "выполняется";
    public static final String CANCELED = "отменена";
    public static final String STOPPED = "приостановлена";
    public static final String CLOSED = "закрыта";

    public EmployeeRequest(int id, String emp_fio, String request_date, String phone, String declarant_fio, String info,
                           int request_priority, String date_of_realization, RequestStatus status, String reason,
                           int cod, RequestType type, String post, String date_of_reg, int reg_user, int zaavitel,
                           RequestBuildingKfu building_kfu, String room_number, String image, String descr) {
        this.id = id;
        this.emp_fio = emp_fio;
        this.request_date = request_date;
        this.phone = phone;
        this.declarant_fio = declarant_fio;
        this.info = info;
        this.request_priority = request_priority;
        this.date_of_realization = date_of_realization;
        this.status = status;
        this.reason = reason;
        this.cod = cod;
        this.type = type;
        this.post = post;
        this.date_of_reg = date_of_reg;
        this.reg_user = reg_user;
        this.zaavitel = zaavitel;
        this.building_kfu = building_kfu;
        this.room_number = room_number;
        this.image = image;
        this.descr = descr;
    }

    public EmployeeRequest(){}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmp_fio() {
        return emp_fio;
    }

    public void setEmp_fio(String emp_fio) {
        this.emp_fio = emp_fio;
    }

    public String getRequest_date() {
        return request_date;
    }

    public void setRequest_date(String request_date) {
        this.request_date = request_date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeclarant_fio() {
        return declarant_fio;
    }

    public void setDeclarant_fio(String declarant_fio) {
        this.declarant_fio = declarant_fio;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getRequest_priority() {
        return request_priority;
    }

    public void setRequest_priority(int request_priority) {
        this.request_priority = request_priority;
    }

    public String getDate_of_realization() {
        return date_of_realization;
    }

    public void setDate_of_realization(String date_of_realization) {
        this.date_of_realization = date_of_realization;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getDate_of_reg() {
        return date_of_reg;
    }

    public void setDate_of_reg(String date_of_reg) {
        this.date_of_reg = date_of_reg;
    }

    public int getReg_user() {
        return reg_user;
    }

    public void setReg_user(int reg_user) {
        this.reg_user = reg_user;
    }

    public int getZaavitel() {
        return zaavitel;
    }

    public void setZaavitel(int zaavitel) {
        this.zaavitel = zaavitel;
    }

    public RequestBuildingKfu getBuilding_kfu() {
        return building_kfu;
    }

    public void setBuilding_kfu(RequestBuildingKfu building_kfu) {
        this.building_kfu = building_kfu;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
