package com.example.androidbarberbooking.Interface;

import com.example.androidbarberbooking.Model.Banner;

import java.util.List;

public interface ILookbookLoadListener  {
    void onLookbookLoadSuccess(List<Banner> banners);
    void onLookbookLoadFailed(String message);
}
