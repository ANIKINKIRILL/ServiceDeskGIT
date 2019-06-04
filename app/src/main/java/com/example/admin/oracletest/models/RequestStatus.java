package com.example.admin.oracletest.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Request status
 */

public class RequestStatus {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("status_name")
    @Expose
    private String status_name;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("descr")
    @Expose
    private String descr;

    public RequestStatus(int id, String status_name, String color, String descr) {
        this.id = id;
        this.status_name = status_name;
        this.color = color;
        this.descr = descr;
    }

    public RequestStatus(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
