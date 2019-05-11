package com.arfeenkhan.registerationappforUser.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arfeenkhan.registerationappforUser.Model.SignleCoachDataModel;
import com.arfeenkhan.registerationappforUser.R;

import java.util.ArrayList;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.RecentAdapterViewHolder> {
    private ArrayList<SignleCoachDataModel> list;
    private Context context;

    public RecentAdapter(ArrayList<SignleCoachDataModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecentAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recent_item, viewGroup, false);

        return new RecentAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentAdapterViewHolder holder, int i) {
        SignleCoachDataModel signleCoachDataModel = list.get(i);
        holder.name.setText(signleCoachDataModel.getUsername());
        holder.uniqeno.setText(signleCoachDataModel.getUserid());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecentAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView name, uniqeno;

        public RecentAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            uniqeno = itemView.findViewById(R.id.id);
        }
    }
}
