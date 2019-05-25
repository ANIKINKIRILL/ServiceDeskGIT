package com.test.admin.servicedesk;

import com.test.admin.servicedesk.Models.EmployeeRequest;

import java.util.ArrayList;

/**
 * Интерфейс для передачи списка найденных заявок с
 * {@link com.test.admin.servicedesk.Fragment.SearchRequestsFragment} на
 * {@link com.test.admin.servicedesk.Fragment.ViewSearchRequestsFragment}
 */

public interface OnViewSearchRequestsFragmentListener {
    void setRequests(ArrayList<EmployeeRequest> requests);
}
