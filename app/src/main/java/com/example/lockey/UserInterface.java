package com.example.lockey;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserInterface {
    @GET("User/{userID}")
    Call<List<User>> getUserById(@Path("userID") int userID);

    @GET("User/byUsername/{username}")
    Call<User> getUserByUsername(@Path("username") String username);

    @POST("User}")
    Call<List<User>> createUser(@Body User newUser);

    @PUT("User/AddDevice/{userid}")
    Call<User> addDeviceToUser(@Path("userid") int userid,@Body int deviceID);
    @PUT("User/RemoveDevice/{userid}")
    Call<User> removeDeviceToUser(@Path("userid") int userid,@Body int deviceID);

    @DELETE("User/{userID}")
    Call<User> removeUser(@Path("userID") int userID);
}