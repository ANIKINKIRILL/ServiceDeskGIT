package com.example.admin.oracletest.models;

import android.content.SharedPreferences;

import com.example.admin.oracletest.BaseApplication;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * User. This class represents engineer in the ServiceDesk
 */

public class User {

    /*---------------------- Retrofit ------------------------*/

    @SerializedName("successful")
    @Expose
    private boolean successful;

    @SerializedName("user_id")
    @Expose
    private int userId;

    @SerializedName("employee_id")
    @Expose
    private int employee_id;

    @SerializedName("firstname")
    @Expose
    private String firstname;

    @SerializedName("lastname")
    @Expose
    private String middlename;

    @SerializedName("middlename")
    @Expose
    private String lastname;

    /*---------------------- User Data ----------------------*/

    public static int myRequestsAmount = 0;

    public static String userLogin;
    public static String userPassword;

    public User(boolean successful, int userId, int employee_id, String firstname, String middlename, String lastname) {
        this.successful = successful;
        this.userId = userId;
        this.employee_id = employee_id;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
    }

    public User(){}

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
