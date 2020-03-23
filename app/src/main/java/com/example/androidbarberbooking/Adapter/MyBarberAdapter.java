package com.example.androidbarberbooking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidbarberbooking.Interface.IRecyclerItemSelectedListener;
import com.example.androidbarberbooking.Model.Barber;
import com.example.androidbarberbooking.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MyBarberAdapter extends RecyclerView.Adapter<MyBarberAdapter.MyViewHolder> {

    Context context;
    List<Barber> barberList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyBarberAdapter(Context context, List<Barber> barberList) {
        this.context = context;
        this.barberList = barberList;
        this.cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_barber , parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_barber_name.setText(barberList.get(position).getName());
        holder.ratingBar.setRating((float)barberList.get(position).getRating());
        if(!cardViewList.contains(holder.card_barber))
            cardViewList.add(holder.card_barber);

    }

    @Override
    public int getItemCount() {
        return barberList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_barber_name;
        RatingBar ratingBar;
        CardView card_barber;


        /// I WAS HERE
        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_barber = (CardView)itemView.findViewById(R.id.card_baber);
            this.txt_barber_name = (TextView)itemView.findViewById(R.id.txt_barber_name);
            ratingBar = (RatingBar)itemView.findViewById(R.id.rtb_barber);
        }
    }
}
