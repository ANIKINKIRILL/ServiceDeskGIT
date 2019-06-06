package com.example.admin.oracletest.ui.main.search;

import com.example.admin.oracletest.models.EmployeeRequest;

import java.util.List;

/**
 * Интерфейс для передачи списка найденных заявок с
 * {@link SearchFragment} на
 * {@link ViewSearchFragment}
 */

public interface OnViewSearchRequestsFragmentListener {
    void setRequests(List<EmployeeRequest> requests);
}
