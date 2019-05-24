package com.example.admin.oracletest.Utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.oracletest.Models.EmployeeRequest;

import java.util.ArrayList;

/**
 * RecyclerViewAdapter для отображения найденных заявок
 */

public class SearchRequestsFragmentRecyclerViewAdapter extends RecyclerView.Adapter<SearchRequestsFragmentRecyclerViewAdapter.ViewHolder> {

    private ArrayList<EmployeeRequest> employeeRequests = new ArrayList<>();

    class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public SearchRequestsFragmentRecyclerViewAdapter(ArrayList<EmployeeRequest> employeeRequests) {
        this.employeeRequests = employeeRequests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
