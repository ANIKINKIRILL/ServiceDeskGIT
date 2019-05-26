package com.test.admin.servicedesk;

public interface MyRequestsFragmentContact {
    interface View {
        void userDoesNotHaveRequests();
        void userHasMoreThanOnePageRequests();
        void userHasLessThanOnePageRequests();
    }
}
