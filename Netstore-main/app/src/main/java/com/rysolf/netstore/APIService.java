package com.rysolf.netstore;

import com.rysolf.netstore.Notifications.MyResponse;
import com.rysolf.netstore.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=BMCUAEgH5wfUCQNNK9l4-MfYvIW3ihvXsnoWHkmu_LoL3yXhzd0jvMPgutJSfAarviqJ3Fg4Y5cJnX4oJ3ib1Uc"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
