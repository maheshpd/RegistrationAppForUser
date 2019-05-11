package com.arfeenkhan.registerationappforUser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.arfeenkhan.registerationappforUser.Model.CityModel;
import com.arfeenkhan.registerationappforUser.R;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CityAdapter extends BaseAdapter {
    Context context;
    ArrayList<CityModel> citylist;
    LayoutInflater inflater;

    public CityAdapter(Context context, ArrayList<CityModel> citylist) {
        this.context = context;
        this.citylist = citylist;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return citylist.size();
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
        convertView = inflater.inflate(R.layout.city_item, null); //inflate the layout
        CircleImageView imageView = convertView.findViewById(R.id.cityimage);
        TextView cityname = convertView.findViewById(R.id.cityname);
        CityModel cityModel = citylist.get(position);
        cityname.setText(cityModel.getName());
        return convertView;
    }
}
