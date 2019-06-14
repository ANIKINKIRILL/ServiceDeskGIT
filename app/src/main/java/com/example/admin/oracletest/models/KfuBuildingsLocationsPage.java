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

public class KfuBuildingsLocationsPage {

    @SerializedName("locations_amount")
    @Expose
    private int locations_amount;

    @SerializedName("locations")
    @Expose
    private KfuBuildingLocation[] locations;

    public KfuBuildingsLocationsPage(int locations_amount, KfuBuildingLocation[] locations) {
        this.locations_amount = locations_amount;
        this.locations = locations;
    }

    public KfuBuildingsLocationsPage(){}

    public int getLocations_amount() {
        return locations_amount;
    }

    public void setLocations_amount(int locations_amount) {
        this.locations_amount = locations_amount;
    }

    public KfuBuildingLocation[] getLocations() {
        return locations;
    }

    public void setLocations(KfuBuildingLocation[] locations) {
        this.locations = locations;
    }
}
