package com.example.androidbarberbooking.Interface;

import com.example.androidbarberbooking.Model.Salon;

import java.util.List;

public interface IBranchLoadListener  {
    void onBranchLoadSuccess(List<Salon> salonList);
    void onBranchLoadFailed(String message);
}
