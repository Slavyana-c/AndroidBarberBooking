package com.example.androidbarberbooking.Retrofit;

import com.example.androidbarberbooking.Model.FCMResponse;

import java.util.Observable;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key="
    })
    @POST("fcm/send")
    Observable<FCMResponse> sendNotification(@Body FCMSendData body);

}
