package com.example.admin.oracletest.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.admin.oracletest.R;

import java.util.ArrayList;

/**
 * Адаптер для отображения характеристик с checkbox на
 * {@link com.example.admin.oracletest.Fragment.DetailzationDoneJobBottomSheetFragment}
 */

public class DetailzationDoneJobBottomSheetFragmentRecyclerViewAdapter extends
        RecyclerView.Adapter<DetailzationDoneJobBottomSheetFragmentRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> detailzationChoices = new ArrayList<>();
    private Context context;

    public DetailzationDoneJobBottomSheetFragmentRecyclerViewAdapter(ArrayList<String> detailzationChoices, Context context) {
        this.detailzationChoices = detailzationChoices;
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
    }

    @Override
    public int getItemCount() {
        return detailzationChoices.size();
    }
}
