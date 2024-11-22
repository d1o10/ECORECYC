package com.capstone.ecorecyc

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Check if message contains notification payload
        remoteMessage.notification?.let {
            sendNotification(it.title, it.body)
        }

        // Handle data payload if necessary (e.g., pass specific data for notifications)
        remoteMessage.data.isNotEmpty().let {
            val title = remoteMessage.data["title"]
            val body = remoteMessage.data["body"]
            sendNotification(title, body)
        }
    }

    private fun sendNotification(title: String?, body: String?) {
        // Define the intent to open the Notifications Activity
        val notificationIntent = Intent(this, Notifications::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        // Set up a pending intent
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Build the notification
        val notification = NotificationCompat.Builder(this, "cleanup_event_notifications")
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.baseline_notifications_24) // Replace with your icon
            .setContentIntent(pendingIntent)  // PendingIntent to open Notifications Activity
            .setAutoCancel(true)
            .build()

        // Show the notification
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)
    }
}
