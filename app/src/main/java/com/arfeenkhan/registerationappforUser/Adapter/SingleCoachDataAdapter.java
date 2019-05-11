package com.arfeenkhan.registerationappforUser.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arfeenkhan.registerationappforUser.Model.CityModel;
import com.arfeenkhan.registerationappforUser.Model.SignleCoachDataModel;
import com.arfeenkhan.registerationappforUser.R;
import com.arfeenkhan.registerationappforUser.Utils.Common;

import java.util.ArrayList;

public class SingleCoachDataAdapter extends BaseAdapter {

    private ArrayList<SignleCoachDataModel> list;
    private Context context;
    LayoutInflater inflater;

    public SingleCoachDataAdapter(ArrayList<SignleCoachDataModel> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = (LayoutInflater.from(context));
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.singlecoachdata_item, null); //inflate the layout
        TextView coachname = convertView.findViewById(R.id.coachname);
        TextView username = convertView.findViewById(R.id.username);
        TextView userphone = convertView.findViewById(R.id.phone);
        TextView userid = convertView.findViewById(R.id.uniqueno);
        TextView date = convertView.findViewById(R.id.date);
        TextView time = convertView.findViewById(R.id.time);
        SignleCoachDataModel signleCoachDataModel = list.get(position);
        coachname.setText(signleCoachDataModel.getCoachname());
        username.setText(signleCoachDataModel.getUsername());
        userphone.setText(signleCoachDataModel.getUserphone());
        userid.setText(signleCoachDataModel.getUserid());
        date.setText(signleCoachDataModel.getDate());
        time.setText(signleCoachDataModel.getTime());
        return convertView;
    }


}
