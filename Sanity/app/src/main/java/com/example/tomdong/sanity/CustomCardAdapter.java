package com.example.tomdong.sanity;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import Model.Category;

/**
 * Created by User on 4/4/2017.
 */

public class CustomCardAdapter extends ArrayAdapter<Category_card> {

    private static final String TAG = "CustomListAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView CategoryType;
        ProgressBar PBar;
        TextView CatProgressText;
    }

    public CustomCardAdapter(Context context, int resource, ArrayList<Category_card> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the persons information
        String CatoryType = getItem(position).GetCatoryType();
        double curr = getItem(position).GetCatCurr();
        double limit = getItem(position).GetCatLimit();
        try {
            final View result;
            final ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(mResource, parent, false);
                holder = new ViewHolder();
                holder.CategoryType = (TextView) convertView.findViewById(R.id.cat_name);
                holder.PBar = (ProgressBar) convertView.findViewById(R.id.bar_in_card);
                holder.CatProgressText = (TextView) convertView.findViewById(R.id.cat_progress_text);
                result = convertView;
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
                result = convertView;
            }

            lastPosition = position;

            holder.CategoryType.setText(CatoryType);
            holder.PBar.setProgress((int)((curr/limit) * 100));
            holder.CatProgressText.setText("$" + curr + "/" + "$" + limit);

            return convertView;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "getView: IllegalArgumentException: " + e.getMessage());
            return convertView;
        }

    }
    public void Remove(int Position)
    {
        Log.e(TAG, "Delete Position: "+getItem(Position).GetBudgetType());
        remove(getItem(Position));
    }

}