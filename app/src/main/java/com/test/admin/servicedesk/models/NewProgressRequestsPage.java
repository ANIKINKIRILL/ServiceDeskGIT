package com.test.admin.servicedesk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewProgressRequestsPage {

    @SerializedName("new_requests_amount")
    @Expose
    private int new_requests_amount;

    @SerializedName("progress_requests_amount")
    @Expose
    private int progress_requests_amount;

    @SerializedName("all_requests_amount")
    @Expose
    private int all_requests_amount;

    public NewProgressRequestsPage(){}

    public NewProgressRequestsPage(int new_requests_amount, int progress_requests_amount, int all_requests_amount) {
        this.new_requests_amount = new_requests_amount;
        this.progress_requests_amount = progress_requests_amount;
        this.all_requests_amount = all_requests_amount;
    }

    public int getNew_requests_amount() {
        return new_requests_amount;
    }

    public void setNew_requests_amount(int new_requests_amount) {
        this.new_requests_amount = new_requests_amount;
    }

    public int getProgress_requests_amount() {
        return progress_requests_amount;
    }

    public void setProgress_requests_amount(int progress_requests_amount) {
        this.progress_requests_amount = progress_requests_amount;
    }

    public int getAll_requests_amount() {
        return all_requests_amount;
    }

    public void setAll_requests_amount(int all_requests_amount) {
        this.all_requests_amount = all_requests_amount;
    }
}
