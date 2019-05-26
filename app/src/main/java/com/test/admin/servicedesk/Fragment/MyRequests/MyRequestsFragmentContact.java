package com.test.admin.servicedesk.Fragment.MyRequests;

public interface MyRequestsFragmentContact {
    interface View {
        void userDoesNotHaveRequests();
        void userHasMoreThanOnePageRequests();
        void userHasLessThanOnePageRequests();
    }
}
