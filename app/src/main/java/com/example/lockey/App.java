package com.example.lockey;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;

public class App extends Application {
    public static final String channel_Id="notification";
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannlel();
    }

    private void createNotificationChannlel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel= new NotificationChannel(
                    channel_Id,
                    "DoorIsUnlocked",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}
