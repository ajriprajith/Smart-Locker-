package com.example.locker;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    Context context;
    ArrayList<String> randomDataList = new ArrayList<>();


    public Adapter(Context context,ArrayList<String> randomDataList) {
        this.context = context;
        this.randomDataList = randomDataList;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.activity_log,parent,false);
        return new MyViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.logTextView.setText("log data Added");

//        User user=list.get(position)
//        holder.Bus_id.setText(user.getBus_id());
//        holder.Bus_name.setText(user.getBus_name());
//        holder.Destination.setText(user.getDestination());
//        holder.Source.setText(user.getSource());
//        holder.Timing.setText(user.getTiming());
//        holder.St_tkt.setText(user.getStd_ticket());
//        8holder.St_tkt.setText(user.getTiming());
//        holder.Eld_tkt.setText(user.getEld_ticket());

    }

    @Override
    public int getItemCount() {
        return 3;
//        return randomDataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView logTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            logTextView=itemView.findViewById(R.id.logTextView);

//            Bus_id=itemView.findViewById(R.id.rtv_id);
//            Bus_name=itemView.findViewById(R.id.rtv_name);
//            Destination=itemView.findViewById(R.id.rtv_dest);
//            Source=itemView.findViewById(R.id.rtv_source);
//            Timing=itemView.findViewById(R.id.rtv_timing);
//            St_tkt=itemView.findViewById(R.id.rtv_stk);
//            Eld_tkt=itemView.findViewById(R.id.rtv_ekt);
        }
    }
}