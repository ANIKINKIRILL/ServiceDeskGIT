package com.test.admin.servicedesk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This is a JSON page that gets from SERVER
 * using https://portal-dis.kpfu.ru/e-ksu/SERVICEDESK_MOBILE_TEST.get_tech_groups
 */

public class TechGroupsPage {

    @SerializedName("tech_groups_amount")
    @Expose
    private int tech_groups_amount;

    @SerializedName("tech_groups")
    @Expose
    private TechGroup[] techGroups;

    public TechGroupsPage(){}

    public TechGroupsPage(int tech_groups_amount, TechGroup[] techGroups) {
        this.tech_groups_amount = tech_groups_amount;
        this.techGroups = techGroups;
    }

    public int getTech_groups_amount() {
        return tech_groups_amount;
    }

    public void setTech_groups_amount(int tech_groups_amount) {
        this.tech_groups_amount = tech_groups_amount;
    }

    public TechGroup[] getTechGroups() {
        return techGroups;
    }

    public void setTechGroups(TechGroup[] techGroups) {
        this.techGroups = techGroups;
    }
}
