package com.example.lockey;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
class RetrofitClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    // https://futurestud.io/tutorials/retrofit-2-adding-customizing-the-gson-converter
                    // Gson is no longer the default converter
                    .build();
        }
        return retrofit;
    }
}
public class ApiUtils {
    private ApiUtils() {
    }

    private static final String BASE_URL = "https://lockeyapi.azurewebsites.net/";

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
        return RetrofitClient.getClient(BASE_URL).create(UserInterface.class);
    }

}