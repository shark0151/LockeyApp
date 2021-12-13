package com.example.lockey;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {
    private ApiUtils() {
    }

    private static final String BASE_URL = "https://lockeyapi.azurewebsites.net/api/";

    public static DeviceInterface getDeviceService() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        DeviceInterface client = retrofit.create(DeviceInterface.class);
        return client;
    }
    public static UserInterface getUserService() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        UserInterface client = retrofit.create(UserInterface.class);
        return client;
    }

}