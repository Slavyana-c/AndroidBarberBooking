package com.example.androidbarberbooking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidbarberbooking.Common.Common;
import com.example.androidbarberbooking.Model.TimeSlot;
import com.example.androidbarberbooking. R;

import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {

    Context context;
    List<TimeSlot> timeSlotList;


    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_time_slot, parent, false);
        return new MyViewHolder(itemView );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // HEREEEEE
    }

    @Override
    public int getItemCount() {
        return Common.TIME_SLOT_TOTAL ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_time_slot, txt_time_slot_description;
        CardView card_time_slot;

         public MyViewHolder(@NonNull View itemView) {
            super(itemView);
             card_time_slot = (CardView) itemView.findViewById(R.id.card_time_slot);
             txt_time_slot = (TextView) itemView.findViewById(R.id.txt_time_slot);
             txt_time_slot_description = (TextView) itemView.findViewById(R.id.txt_time_slot_description);
        }
    }
}
