package com.test.admin.servicedesk.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.test.admin.servicedesk.R;
import com.test.admin.servicedesk.models.TechGroup;

import java.util.ArrayList;

/**
 * Adapter for tech groups spinner in {@link com.test.admin.servicedesk.ui.main.search.SearchFragment}
 */

public class TechGroupsAdapter extends ArrayAdapter<TechGroup> {

    private Context context;
    private ArrayList<TechGroup> techGroups;

    public TechGroupsAdapter(Context context, int resource, ArrayList<TechGroup> techGroups) {
        super(context, resource, techGroups);
        this.context = context;
        this.techGroups = techGroups;
    }

    @Override
    public int getCount() {
        return techGroups.size();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public TechGroup getItem(int position) {
        return techGroups.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        TechGroupsAdapter.ViewHolder viewHolder;
        TechGroup techGroup = techGroups.get(position);
        if(view != null){
            viewHolder = (TechGroupsAdapter.ViewHolder) view.getTag();
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.tech_group_spinner_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.techGroupName = view.findViewById(R.id.name);
            view.setTag(viewHolder);
        }

        viewHolder.techGroupName.setText(techGroup.getName());

        return view;

    }

    static class ViewHolder {
        TextView techGroupName;
    }

}
