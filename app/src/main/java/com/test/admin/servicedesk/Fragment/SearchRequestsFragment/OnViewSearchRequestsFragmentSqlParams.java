package com.test.admin.servicedesk.Fragment.SearchRequestsFragment;

/**
 * Интерфейс для передачи SQL параметров с
 * {@link SearchRequestsFragment} на
 * {@link ViewSearchRequestsFragment}
 */

public interface OnViewSearchRequestsFragmentSqlParams {
    void setSqlParams(String p_sql_statement, String p_sql_statement_count_rows);
}
