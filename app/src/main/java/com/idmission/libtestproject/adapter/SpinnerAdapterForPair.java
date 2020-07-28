package com.idmission.libtestproject.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.idmission.libtestproject.R;

import java.util.ArrayList;

public class SpinnerAdapterForPair<T> extends ArrayAdapter<Pair<String,T>> {

    ArrayList<Pair<String, T>> mLs;
    Context context;

    public SpinnerAdapterForPair(Context context, int textViewResourceId,
                                 ArrayList<Pair<String, T>> objects) {
        super(context, textViewResourceId, objects);

        mLs = objects;
        this.context = context;
    }

    public ArrayList<Pair<String, T>> getPopulatedList(){
        return mLs;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final TextView filterName;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            filterName = (TextView) layoutInflater.inflate(
                    android.R.layout.simple_spinner_dropdown_item, parent, false);
        } else {
            filterName = (TextView) convertView;
        }

        if(position < mLs.size()){
            filterName.setText(mLs.get(position).first);
            filterName.setTag(mLs.get(position).second);
        }

        return filterName;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        final TextView filterName;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // filterName = (TextView) layoutInflater.inflate(R.layout.sherlock_spinner_dropdown_item, parent, false);
            filterName = (TextView) layoutInflater.inflate(
                    android.R.layout.simple_spinner_dropdown_item, parent, false);
            //filterName.setGravity(Gravity.CENTER);
            filterName.setTextColor(context.getResources().getColor(R.color.black));
            //filterName.setEllipsize(TextUtils.TruncateAt.END);
        } else {
            filterName = (TextView) convertView;
        }

        filterName.setText(getItem(position).first);
        return filterName;
    }
}
