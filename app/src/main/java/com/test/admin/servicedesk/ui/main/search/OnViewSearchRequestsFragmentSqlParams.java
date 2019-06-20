package com.test.admin.servicedesk.ui.main.search;

/**
 * Интерфейс для передачи SQL параметров с
 * {@link SearchFragment} на
 * {@link ViewSearchFragment}
 */

public interface OnViewSearchRequestsFragmentSqlParams {
    void setSqlParams(String p_sql_statement, String p_sql_statement_count_rows);
}