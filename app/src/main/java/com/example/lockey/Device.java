package com.example.lockey;


import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
public class Device implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("totalComments")
    @Expose
    private Integer totalComments;
    public Device(){}

    public Device(Integer id, String content, String user, Integer totalComments)
    {
        this.id= id;
        this.content=content;
        this.user=user;
        this.totalComments=totalComments;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(Integer totalComments) {
        this.totalComments = totalComments;
    }

    @NonNull
    @Override
    public String toString() {
        return id + ": " + content + " " + user + ", " + totalComments;
    }


}