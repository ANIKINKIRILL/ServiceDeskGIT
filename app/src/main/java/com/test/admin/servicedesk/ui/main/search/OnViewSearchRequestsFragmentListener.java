package com.test.admin.servicedesk.ui.main.search;

import com.test.admin.servicedesk.models.EmployeeRequest;

import java.util.List;

/**
 * Интерфейс для передачи списка найденных заявок с
 * {@link SearchFragment} на
 * {@link ViewSearchFragment}
 */

public interface OnViewSearchRequestsFragmentListener {
    void setRequests(List<EmployeeRequest> requests);
}
