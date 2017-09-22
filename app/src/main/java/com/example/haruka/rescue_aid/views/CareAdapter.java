package com.example.haruka.rescue_aid.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.haruka.rescue_aid.R;
import com.example.haruka.rescue_aid.utils.Care;

import java.util.ArrayList;

/**
 * Created by Tomoya on 9/22/2017 AD.
 */

public class CareAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<Care> careList;

    public CareAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setCareList(ArrayList<Care> careList) {
        this.careList = careList;
    }

    @Override
    public int getCount() {
        return careList.size();
    }

    @Override
    public Object getItem(int position) {
        return careList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long)careList.get(position).index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.list_care,parent,false);

        ((TextView)convertView.findViewById(R.id.textview_care_title)).setText(careList.get(position).name);
        ((TextView)convertView.findViewById(R.id.textview_care_description)).setText(careList.get(position).description);
        ((Button)convertView.findViewById(R.id.btn_explain_care)).setText("手当をする");

        return convertView;
    }
}