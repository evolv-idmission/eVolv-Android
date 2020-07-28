package com.idmission.libtestproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.idmission.libtestproject.R;
import com.idmission.libtestproject.fragments.DataCapture;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomListViewAdapter extends BaseAdapter {

    private Activity activity;
    private static LayoutInflater inflater = null;

    public CustomListViewAdapter(Activity activity) {
        this.activity = activity;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return DataCapture.mapAddKeyValue.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null)
            view = inflater.inflate(R.layout.custom_list_row, null);

        TextView keyTextView = (TextView) view.findViewById(R.id.text_view_key);
        TextView valueTextView = (TextView) view.findViewById(R.id.text_view_value);
        ImageView cancelImageView = (ImageView) view.findViewById(R.id.cancel_record);

        keyTextView.setText(DataCapture.mapAddKeyValue.keySet().toArray()[position].toString());
        valueTextView.setText(DataCapture.mapAddKeyValue.values().toArray()[position].toString());

        cancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != DataCapture.mapAddKeyValue && DataCapture.mapAddKeyValue.size() > 0) {
                    DataCapture.mapAddKeyValue.remove(DataCapture.mapAddKeyValue.keySet().toArray()[position].toString());
                }

                if (null != DataCapture.adapter) {
                    DataCapture.adapter.notifyDataSetChanged();
                }

                if (null != DataCapture.listView) {
                    DataCapture.setListViewHeightBasedOnItems(DataCapture.listView);
                }
            }
        });

        return view;
    }
}