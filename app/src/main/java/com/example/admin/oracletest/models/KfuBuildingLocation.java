package com.example.admin.oracletest.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Kfu building location
 */

public class KfuBuildingLocation {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("adres_name")
    @Expose
    private String adres_name;

    @SerializedName("numerations")
    @Expose
    private int numerations;

    @SerializedName("parent_id")
    @Expose
    private int parent_id;

    @SerializedName("status")
    @Expose
    private int status;

    public KfuBuildingLocation(int id, String adres_name, int numerations, int parent_id, int status) {
        this.id = id;
        this.adres_name = adres_name;
        this.numerations = numerations;
        this.parent_id = parent_id;
        this.status = status;
    }

    public KfuBuildingLocation(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdres_name() {
        return adres_name;
    }

    public void setAdres_name(String adres_name) {
        this.adres_name = adres_name;
    }

    public int getNumerations() {
        return numerations;
    }

    public void setNumerations(int numerations) {
        this.numerations = numerations;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
