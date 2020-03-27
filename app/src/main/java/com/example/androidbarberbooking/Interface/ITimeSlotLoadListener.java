package com.example.androidbarberbooking.Interface;

import com.example.androidbarberbooking.Model.TimeSlot;

import java.util.List;

public interface ITimeSlotLoadListener {
    void onTimeSlotLoadSuccess(List<TimeSlot> salonList);
    void onTimeSlotLoadFailed(String message);
    void onTimeSlotLoadEmpty();
}
