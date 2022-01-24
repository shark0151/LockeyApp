package com.example.lockey;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserInterface {
    @GET("User/{userID}")
    Call<User> getUserById(@Path("userID") int userID);

    @GET("User/byUsername/{username}")
    Call<User> getUserByUsername(@Path("username") String username);

    @POST("User")
    Call<User> createUser(@Body User newUser);

    @Headers({"Content-Type: application/json"})
    @PUT("User/AddDevice")
    Call<User> addDeviceToUser(@Body User mac);
    @PUT("User/RemoveDevice")
    Call<User> removeDeviceToUser(@Body User usr);

    @DELETE("User/{userID}")
    Call<User> removeUser(@Path("userID") int userID);

    @GET("User/GetUserDevices/{userid}")
    Call<List<String>> getDevicesForUser(@Path("userid") int userid);
}