package com.example.admin.oracletest.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Service Desk tech group from TECH_CENTER$DB.TECH_GROUP table
 */

public class TechGroup {

    @SerializedName("cod")
    @Expose
    private int cod;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("parent_cod")
    @Expose
    private int parent_cod;

    @SerializedName("office_id")
    @Expose
    private int office_id;

    @SerializedName("identity_cod")
    @Expose
    private String identity_cod;

    public TechGroup(){}

    public TechGroup(int cod, String name, int status, int parent_cod, int office_id, String identity_cod) {
        this.cod = cod;
        this.name = name;
        this.status = status;
        this.parent_cod = parent_cod;
        this.office_id = office_id;
        this.identity_cod = identity_cod;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getParent_cod() {
        return parent_cod;
    }

    public void setParent_cod(int parent_cod) {
        this.parent_cod = parent_cod;
    }

    public int getOffice_id() {
        return office_id;
    }

    public void setOffice_id(int office_id) {
        this.office_id = office_id;
    }

    public String getIdentity_cod() {
        return identity_cod;
    }

    public void setIdentity_cod(String identity_cod) {
        this.identity_cod = identity_cod;
    }
}
