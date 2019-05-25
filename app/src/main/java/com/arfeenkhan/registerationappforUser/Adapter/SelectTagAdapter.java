package com.arfeenkhan.registerationappforUser.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arfeenkhan.registerationappforUser.Activity.ChooseCoachName;
import com.arfeenkhan.registerationappforUser.Activity.UserDetails;
import com.arfeenkhan.registerationappforUser.Activity.UserRegistertionActive;
import com.arfeenkhan.registerationappforUser.Model.SelectTagModel;
import com.arfeenkhan.registerationappforUser.R;
import com.arfeenkhan.registerationappforUser.Utils.Common;

import java.util.ArrayList;

public class SelectTagAdapter extends RecyclerView.Adapter<SelectTagAdapter.TagViewHolder> {
    Context ctx;
    ArrayList<SelectTagModel> list;

    public SelectTagAdapter(Context ctx, ArrayList<SelectTagModel> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_tag_item, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TagViewHolder holder, final int position) {
        SelectTagModel stm = list.get(position);
        holder.mname.setText(stm.getName());
        holder.mplace.setText(stm.getPlace());
        holder.mctf.setText(stm.getCtf());
        holder.mtime.setText(stm.getTime());
        holder.mtags.setText(stm.getTagno());
        holder.mdate.setText(stm.getDate());

        final String place = holder.mplace.getText().toString();
        final String name = holder.mname.getText().toString();
        final String ctf = holder.mctf.getText().toString();
        final String time = holder.mtime.getText().toString();
        final String date = holder.mdate.getText().toString();
        final String tag = holder.mtags.getText().toString();

        String tf = list.get(position).getTf();
        String sessionnane = stm.getSessionname();

        if (sessionnane.equals("True")) {
            holder.itemView.setBackgroundColor(Color.GREEN);
            if (tf.equals("True")) {
                holder.itemView.setBackgroundColor(Color.WHITE);
            } else {
                holder.itemView.setBackgroundColor(Color.GREEN);
            }
        } else {
            holder.itemView.setBackgroundColor(Color.RED);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.coachName = holder.mname.getText().toString();
                Common.eventTimes = holder.mtime.getText().toString();
                Common.place = holder.mplace.getText().toString();
                Common.name = holder.mname.getText().toString();
                Common.ctf = holder.mctf.getText().toString();
                Common.tagno = holder.mtags.getText().toString();
                Common.sdate = holder.mdate.getText().toString();
                Common.tf = list.get(position).getTf();


                Intent mainIntent = new Intent(ctx, ChooseCoachName.class);
                mainIntent.putExtra("place", place);
                mainIntent.putExtra("name", name);
                mainIntent.putExtra("ctf", ctf);
                mainIntent.putExtra("time", time);
                mainIntent.putExtra("tag", tag);
                mainIntent.putExtra("date", date);
                ctx.startActivity(mainIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TagViewHolder extends RecyclerView.ViewHolder {
        TextView mplace, mtime, mtags, mctf, mname, mdate, totalnopeople;
        RecyclerView allocationname;

        public TagViewHolder(View itemView) {
            super(itemView);

            mplace = itemView.findViewById(R.id.mplace);
            mtime = itemView.findViewById(R.id.mtime);
            mtags = itemView.findViewById(R.id.mtexttags);
            mctf = itemView.findViewById(R.id.mtype);
            mname = itemView.findViewById(R.id.mname);
            mdate = itemView.findViewById(R.id.mdate);

        }
    }
}

