package com.test.admin.servicedesk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Data that returns back from Kfu Server
 * when getting requests
 */

public class RequestsPage {

    @SerializedName("successful")
    @Expose
    private boolean successful;

    @SerializedName("v_p_start")
    @Expose
    private int v_p_start;

    @SerializedName("v_p_end")
    @Expose
    private int v_p_end;

    @SerializedName("requests")
    @Expose
    private EmployeeRequest requests[];

    @SerializedName("requests_amount")
    @Expose
    private int requests_amount;

    public RequestsPage(boolean successful, int v_p_start, int v_p_end, EmployeeRequest[] requests) {
        this.successful = successful;
        this.v_p_start = v_p_start;
        this.v_p_end = v_p_end;
        this.requests = requests;
    }

    public RequestsPage(){}

    public int getRequests_amount() {
        return requests_amount;
    }

    public void setRequests_amount(int requests_amount) {
        this.requests_amount = requests_amount;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public int getV_p_start() {
        return v_p_start;
    }

    public void setV_p_start(int v_p_start) {
        this.v_p_start = v_p_start;
    }

    public int getV_p_end() {
        return v_p_end;
    }

    public void setV_p_end(int v_p_end) {
        this.v_p_end = v_p_end;
    }

    public EmployeeRequest[] getRequests() {
        return requests;
    }

    public void setRequests(EmployeeRequest[] requests) {
        this.requests = requests;
    }
}
