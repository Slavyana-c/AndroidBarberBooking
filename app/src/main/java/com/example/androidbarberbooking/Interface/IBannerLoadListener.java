package com.example.androidbarberbooking.Interface;

import com.example.androidbarberbooking.Model.Banner;

import java.util.List;

public interface IBannerLoadListener {
    void onBannerLoadSuccess(List<Banner> banners);
    void onBannerLoadFailed(String message);
}
