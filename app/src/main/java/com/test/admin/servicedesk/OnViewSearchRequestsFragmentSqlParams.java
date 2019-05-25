package com.test.admin.servicedesk;

/**
 * Интерфейс для передачи SQL параметров с
 * {@link com.test.admin.servicedesk.Fragment.SearchRequestsFragment} на
 * {@link com.test.admin.servicedesk.Fragment.ViewSearchRequestsFragment}
 */

public interface OnViewSearchRequestsFragmentSqlParams {
    void setSqlParams(String p_sql_statement, String p_sql_statement_count_rows);
}
