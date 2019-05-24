package com.example.admin.oracletest;

/**
 * Интерфейс для передачи SQL параметров с
 * {@link com.example.admin.oracletest.Fragment.SearchRequestsFragment} на
 * {@link com.example.admin.oracletest.Fragment.ViewSearchRequestsFragment}
 */

public interface OnViewSearchRequestsFragmentSqlParams {
    void setSqlParams(String p_sql_statement, String p_sql_statement_count_rows);
}
