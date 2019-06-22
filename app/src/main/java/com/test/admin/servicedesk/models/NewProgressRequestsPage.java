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

}
