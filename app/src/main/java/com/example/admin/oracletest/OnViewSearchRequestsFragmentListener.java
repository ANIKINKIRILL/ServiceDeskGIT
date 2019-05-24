package com.example.admin.oracletest;

import com.example.admin.oracletest.Models.EmployeeRequest;

import java.util.ArrayList;

/**
 * Интерфейс для передачи списка найденных заявок с
 * {@link com.example.admin.oracletest.Fragment.SearchRequestsFragment} на
 * {@link com.example.admin.oracletest.Fragment.ViewSearchRequestsFragment}
 */

public interface OnViewSearchRequestsFragmentListener {
    void setRequests(ArrayList<EmployeeRequest> requests);
}
