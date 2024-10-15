package com.carrental.carspeeddemo.manager

import android.util.Log

/**
 * This is notification class responsible for sending notification via Firebase.
 */
class FirebaseNotificationManagerManager: INotificationManager {

    companion object {
        private const val TAG: String = "FirebaseNotificationManagerManager"
    }

    override fun sendNotification(title: String, message: String, carId: String) {
        // Add Notification logic here.
        Log.d(TAG,"Notification Sent $title, $message")
    }
}