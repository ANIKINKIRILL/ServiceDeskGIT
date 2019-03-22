package com.example.admin.oracletest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.oracletest.Models.EmployeeRequest;

import java.util.ArrayList;

/**
 * Адаптер для отображения заявок
 */

public class EmployeeRequestsRecyclerViewAdapter extends RecyclerView.Adapter<EmployeeRequestsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<EmployeeRequest> requests = new ArrayList<>();
    private Context context;

    public EmployeeRequestsRecyclerViewAdapter(ArrayList<EmployeeRequest> requests, Context context) {
        this.requests = requests;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView requestCode,requestDateOfRegistration,requestDateOfRealization,
                requestZaavitel,requestBuildingKfu_Name,requestBody;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            requestCode = itemView.findViewById(R.id.requestCode);
            requestDateOfRegistration = itemView.findViewById(R.id.requestDateOfRegistration);
            requestDateOfRealization = itemView.findViewById(R.id.requestDateOfRealization);
            requestZaavitel = itemView.findViewById(R.id.requestZaavitel);
            requestBuildingKfu_Name = itemView.findViewById(R.id.requestBuildingKfu_Name);
            requestBody = itemView.findViewById(R.id.requestBody);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.employee_request_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // Получаем EmployeeRequest
        EmployeeRequest request = requests.get(i);
        // Извлекаем данные заявки и устанавливаем их в виджеты
        viewHolder.requestCode.setText("Номер заявки: " + Integer.toString(request.getCod()));
        viewHolder.requestDateOfRegistration.setText("Регистрация: " + request.getRequest_date());
        viewHolder.requestDateOfRealization.setText("Дедлайн: " + request.getDate_of_realization());
        viewHolder.requestZaavitel.setText("Заявитель: " + request.getDeclarant_fio());
        viewHolder.requestBuildingKfu_Name.setText("Адрес: " + request.getBuilding_kfu_name());
        viewHolder.requestBody.setText("Описание: " + request.getInfo());
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }
}
