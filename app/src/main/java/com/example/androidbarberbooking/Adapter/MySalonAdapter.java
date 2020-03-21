package com.example.androidbarberbooking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidbarberbooking.Model.Salon;
import com.example.androidbarberbooking.R;

import org.w3c.dom.Text;

import java.util.List;

public class MySalonAdapter extends RecyclerView.Adapter<MySalonAdapter.MyViewHolder> {

    Context context;
    List<Salon> salonList;

    public MySalonAdapter(Context context, List<Salon> salonList) {
        this.context = context;
        this.salonList = salonList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_salon, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_salon_name.setText(salonList.get(position).getName());
        holder.txt_salon_address.setText(salonList.get(position).getAddress());

    }

    @Override
    public int getItemCount() {
        return salonList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txt_salon_name, txt_salon_address;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_salon_address = (TextView)itemView.findViewById(R.id.txt_salon_address);
            txt_salon_name = (TextView)itemView.findViewById(R.id.txt_salon_name);
        }
    }
}
