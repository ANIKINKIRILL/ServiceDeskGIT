package com.example.admin.oracletest.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.oracletest.Activity.DetailRequestActivity;
import com.example.admin.oracletest.Models.EmployeeRequest;
import com.example.admin.oracletest.R;

import java.util.ArrayList;

/**
 * Адаптер для отображения заявок
 */

public class EmployeeRequestsRecyclerViewAdapter extends RecyclerView.Adapter<EmployeeRequestsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<EmployeeRequest> requests = new ArrayList<>();
    private Context context;

    public EmployeeRequestsRecyclerViewAdapter(Context context, ArrayList<EmployeeRequest> requests) {
        this.requests = requests;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView requestCode,requestDateOfRegistration,requestDateOfRealization,
                requestZaavitel,requestBuildingKfu_Name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            requestCode = itemView.findViewById(R.id.requestCode);
            requestDateOfRegistration = itemView.findViewById(R.id.requestDateOfRegistration);
            requestDateOfRealization = itemView.findViewById(R.id.requestDateOfRealization);
            requestZaavitel = itemView.findViewById(R.id.requestZaavitel);
            requestBuildingKfu_Name = itemView.findViewById(R.id.requestBuildingKfu_Name);
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
        final String cod = Integer.toString(request.getCod());
        final String date_of_reg = request.getRequest_date();
        final String date_of_realization = request.getDate_of_realization();
        final String declarant_fio = request.getDeclarant_fio();
        final String post = request.getPost();
        final String building_kfu_name = request.getBuilding_kfu_name();
        final String roomNumber = request.getRoom_number();
        final String phone = request.getPhone();
        final String body = request.getInfo();
        final String status = request.getStatus_name();
        final String emp_fio = request.getEmp_fio();
        // Устанавливаем данные в виджеты
        viewHolder.requestCode.setText(String.format("%s: %s", context.getString(R.string.requestNumber), cod));
        viewHolder.requestDateOfRegistration.setText(String.format("%s: %s", context.getString(R.string.date_of_reg), date_of_reg));
        viewHolder.requestDateOfRealization.setText(String.format("%s: %s", context.getString(R.string.date_of_realization), date_of_realization));
        viewHolder.requestZaavitel.setText(String.format("%s: %s", context.getString(R.string.zaavitel), declarant_fio));
        viewHolder.requestBuildingKfu_Name.setText(String.format("%s: %s", context.getString(R.string.building_kfu_name), building_kfu_name));
        // Обработка нажатия на карточку
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Открытие активити с более детальной информацией о заявке
                Intent intent = new Intent(context, DetailRequestActivity.class);
                // Передаем в intent данные
                intent.putExtra(context.getString(R.string.requestNumber), cod);
                intent.putExtra(context.getString(R.string.date_of_reg), date_of_reg);
                intent.putExtra(context.getString(R.string.date_of_realization), date_of_realization);
                intent.putExtra(context.getString(R.string.zaavitel), declarant_fio);
                intent.putExtra(context.getString(R.string.post), post);
                intent.putExtra(context.getString(R.string.roomNumber), roomNumber);
                intent.putExtra(context.getString(R.string.phone), phone);
                intent.putExtra(context.getString(R.string.body), body);
                intent.putExtra(context.getString(R.string.building_kfu_name), building_kfu_name);
                intent.putExtra(context.getString(R.string.requestStatus), status);
                intent.putExtra(context.getString(R.string.emp_fio), emp_fio);
                // Переходим на активити с детальной информацией о заявке
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }
}
