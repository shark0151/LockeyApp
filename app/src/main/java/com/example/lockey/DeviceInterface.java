package com.example.lockey;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DeviceInterface {
    @GET("Messages/{postId}/comments")
    Call<List<Device>> getAllComments(@Path("postId") int postId);

    @POST("Messages/{postId}/comments")
    Call<Device> saveCommentBody(@Path("postId") int postId,@Body Device comment);

    @DELETE("Messages/{postId}/comments/{id}")
    Call<Device> deleteComment(@Path("postId") int postId,@Path("id") int id);
}
