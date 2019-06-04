package com.example.admin.oracletest.ui.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.oracletest.R;
import com.example.admin.oracletest.models.EmployeeRequest;

import java.util.ArrayList;
import java.util.List;

public class RequestsPageRecyclerAdapter extends RecyclerView.Adapter<RequestsPageRecyclerAdapter.ViewHolder> {

    private List<EmployeeRequest> employeeRequestList;

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView requestCodeValue,requestDateOfRegistrationValue,requestDateOfRealizationValue,
                requestZaavitelValue,requestBuildingKfu_NameValue;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            requestCodeValue = itemView.findViewById(R.id.requestCodeValue);
            requestDateOfRegistrationValue = itemView.findViewById(R.id.requestDateOfRegistrationValue);
            requestDateOfRealizationValue = itemView.findViewById(R.id.requestDateOfRealizationValue);
            requestZaavitelValue = itemView.findViewById(R.id.requestZaavitelValue);
            requestBuildingKfu_NameValue = itemView.findViewById(R.id.requestBuildingKfu_NameValue);
        }
    }

    public RequestsPageRecyclerAdapter(List<EmployeeRequest> employeeRequestList) {
        this.employeeRequestList = employeeRequestList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employee_request_item, viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // Получаем EmployeeRequest
        EmployeeRequest request = employeeRequestList.get(i);
        // Извлекаем данные заявки и устанавливаем их в виджеты
        final String cod = Integer.toString(request.getCod());
        final String date_of_reg = request.getRequest_date();
        final String date_of_realization = request.getDate_of_realization();
        final String declarant_fio = request.getDeclarant_fio();
        final String post = request.getPost();
        final String building_kfu_name = request.getBuilding_kfu().getName();
        final String roomNumber = request.getRoom_number();
        final String phone = request.getPhone();
        final String body = request.getInfo();
        final String status = request.getStatus().getStatus_name();
        final String emp_fio = request.getEmp_fio();
        // Устанавливаем данные в виджеты
        viewHolder.requestCodeValue.setText(cod);
        viewHolder.requestDateOfRegistrationValue.setText(date_of_reg);
        viewHolder.requestDateOfRealizationValue.setText(date_of_realization);
        viewHolder.requestZaavitelValue.setText(declarant_fio);
        viewHolder.requestBuildingKfu_NameValue.setText(building_kfu_name);
        // Обработка нажатия на карточку
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
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
                */
                Toast.makeText(viewHolder.itemView.getContext(), status, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return employeeRequestList.size();
    }

}
