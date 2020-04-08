package com.example.androidbarberbooking.Retrofit;

import com.example.androidbarberbooking.Model.FCMResponse;
import com.example.androidbarberbooking.Model.FCMSendData;


import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMApi {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAxKFi3is:APA91bFtAbktaL-bxcXjo9_wZLM5Oy6gSPgrCZz5SHGbRNUJALBD_6cCrNRfG36rC6un-SxpH2HngLB8m5d3eQO7BM1wHDro1wlzEk90JNqbzW00ykI2G66MSQWnH73T81xk24tGjgh6"
    })
    @POST("fcm/send")
    Observable<FCMResponse> sendNotification(@Body FCMSendData body);

}
