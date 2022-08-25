package com.example.collegedocumentsharing;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {

    final String CHANNEL_ID = "HEADS_UP_NOTIFICATION";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {

    }
}
