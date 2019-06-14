package com.example.admin.oracletest.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * JSON class for building kfu location names
 * When user got authenticated in Application
 * then on background thread starts loading locations names
 * from server so then when user wants to search requests by
 * address/kfu building location it already be in autocompletetextview
 */

public class Locations {

    @SerializedName("locations")
    @Expose
    private String[] locations;

    public Locations(String[] locations) {
        this.locations = locations;
    }

    public Locations(){}

    public String[] getLocations() {
        return locations;
    }

    public void setLocations(String[] locations) {
        this.locations = locations;
    }
}
