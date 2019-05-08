package com.example.admin.oracletest.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.oracletest.Activity.DetailzationDoneJobActivity;
import com.example.admin.oracletest.R;

import java.util.ArrayList;

/**
 * Адаптер для отображения детализации выполненных работ на
 * {@link DetailzationDoneJobActivity}
 */

public class DetailzationDoneJobRecyclerViewAdapter extends RecyclerView.Adapter<DetailzationDoneJobRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> detailzationChoices = new ArrayList<>();
    private Context context;

    public DetailzationDoneJobRecyclerViewAdapter(ArrayList<String> detailzationChoices, Context context) {
        this.detailzationChoices = detailzationChoices;
        this.context = context;
    }

    public DetailzationDoneJobRecyclerViewAdapter(){}

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView detailzation_text;
        ImageView deleteButton;
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            detailzation_text = itemView.findViewById(R.id.detailzation_text);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.recyclerview_item_detailzation_done_job, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String detailzationChoice = detailzationChoices.get(i);
        viewHolder.detailzation_text.setText(detailzationChoice);
        viewHolder.deleteButton.setOnClickListener((v) -> {
            detailzationChoices.remove(detailzationChoice);
            //notifyDataSetChanged();
            notifyItemRemoved(viewHolder.getLayoutPosition());
        });
    }

    @Override
    public int getItemCount() {
        return detailzationChoices.size();
    }

}
