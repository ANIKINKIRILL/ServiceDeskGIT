package com.test.admin.servicedesk.Fragment.SearchRequestsFragment;

import com.test.admin.servicedesk.Models.EmployeeRequest;

import java.util.ArrayList;

/**
 * Интерфейс для передачи списка найденных заявок с
 * {@link SearchRequestsFragment} на
 * {@link ViewSearchRequestsFragment}
 */

public interface OnViewSearchRequestsFragmentListener {
    void setRequests(ArrayList<EmployeeRequest> requests);
}
