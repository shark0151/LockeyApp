package com.example.lockey;


import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Device implements Serializable {
    @SerializedName("ID")
    @Expose
    private String Id;
    @SerializedName("IsLocked")
    @Expose
    private Boolean IsLocked;
    @SerializedName("Time")
    @Expose
    private String Time;

    public Device(){}

    public Device(String id, Boolean isLocked, String time)
    {
        this.Id= id;
        this.IsLocked=isLocked;
        this.Time=time;
    }



    public String getId() {
        return Id;
    }

    public void setId(String  id) {
        this.Id = id;
    }

    public Boolean getIsLocked() {
        return IsLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.IsLocked = isLocked;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        this.Time = time;
    }



    @NonNull
    @Override
    public String toString() {
        return Id + ": " + IsLocked + " " + Time;
    }


}