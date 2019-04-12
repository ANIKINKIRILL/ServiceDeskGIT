package com.example.admin.oracletest.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.admin.oracletest.Activity.DetailzationDoneJobActivity;
import com.example.admin.oracletest.R;

import java.util.ArrayList;

/**
 * Адаптер для отображения характеристик с checkbox на
 * {@link com.example.admin.oracletest.Fragment.DetailzationDoneJobBottomSheetFragment}
 */

public class DetailzationDoneJobBottomSheetFragmentRecyclerViewAdapter extends
        RecyclerView.Adapter<DetailzationDoneJobBottomSheetFragmentRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "DetailzationDoneJobBott";

    // Переменные
    private ArrayList<String> detailzationChoices = new ArrayList<>();
    private ArrayList<String> userDetailzationChoices = new ArrayList<>();
    private Context context;

    public DetailzationDoneJobBottomSheetFragmentRecyclerViewAdapter(ArrayList<String> detailzationChoices,ArrayList<String> userDetailzationChoices, Context context) {
        this.detailzationChoices = detailzationChoices;
        this.userDetailzationChoices = userDetailzationChoices;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checkBox;
        TextView detailzation_text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            detailzation_text = itemView.findViewById(R.id.detailzation_text);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_item_detailzation_done_job_bottomsheet_fragment, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String detailzationChoice = detailzationChoices.get(i);
        viewHolder.detailzation_text.setText(detailzationChoice);

        // Реализация нажатия на checkbox
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    DetailzationDoneJobActivity.userDetailzationChoices.add(detailzationChoice);
                }else{
                    DetailzationDoneJobActivity.userDetailzationChoices.remove(detailzationChoice);
                    Log.d(TAG, "onCheckedChanged: " + DetailzationDoneJobActivity.userDetailzationChoices.size());
                }
            }
        });

        // Проверка если польватель уже выбрал эту характеристику, то checkbox -> checked
        if(userDetailzationChoices.contains(detailzationChoice)){
            viewHolder.checkBox.setChecked(true);
        }

    }

    @Override
    public int getItemCount() {
        return detailzationChoices.size();
    }
}
