package com.example.lockey;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DeviceInterface {
    @GET("Sensor/{DeviceID}")
    Call<List<Device>> getAllValuesForDevice(@Path("DeviceID") String DeviceID);

    @DELETE("Sensor/{DeviceID}")
    Call<Device> deleteValuesForSensor(@Path("DeviceID") String DeviceID);
}
