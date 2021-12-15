package com.example.lockey;


import androidx.annotation.NonNull;
public class User{

    private int ID;
    private String Username;
    private String Password;
    private String DeviceConnected;

    public User(){}

    public User(int id, String username, String password, String deviceConnected)
    {
        this.ID= id;
        this.Username=username;
        this.Password=password;
        this.DeviceConnected=deviceConnected;
    }



    public int getId() {
        return ID;
    }

    public void setId(int id) {
        this.ID = id;
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
        return ID + ": " + Username + ", " + DeviceConnected;
    }
}
