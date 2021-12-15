package com.example.lockey;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DeviceInterface {
    @GET("GetUserDevices/{deviceid}")
    Call<List<Device>> getAllValuesForDevice(@Path("deviceid") int deviceid);

    @DELETE("Sensor/{id}")
    Call<Device> deleteValuesForSensor(@Path("id") int id);
}
