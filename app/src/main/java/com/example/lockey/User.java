package com.example.lockey;


import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
public class User implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("user")
    @Expose
    private String Username;

    @SerializedName("password")
    @Expose
    private String Password;

    @SerializedName("devices")
    @Expose
    private String DeviceConnected;

    public User(){}

    public User(Integer id, String user, String pass, String devices)
    {
        this.id= id;
        this.Username=user;
        this.Password=pass;
        this.DeviceConnected=devices;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getDeviceConnected() {
        return DeviceConnected;
    }

    public void setDeviceConnected(String deviceConnected) { this.DeviceConnected = deviceConnected; }

    @NonNull
    @Override
    public String toString() {
        return id + ": " + Username + ", " + DeviceConnected;
    }
}
