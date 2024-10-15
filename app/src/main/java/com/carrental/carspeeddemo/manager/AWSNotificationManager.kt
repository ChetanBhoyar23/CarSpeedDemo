package com.carrental.carspeeddemo.manager

import android.util.Log

/**
 * AWS notification manager.
 */
class AWSNotificationManager : INotificationManager {

    companion object {
        private const val TAG: String = "AWSNotificationManagerManager"
    }

    override fun sendNotification(title: String, message: String, carId: String) {
        // Add Notification logic here.
        Log.d(TAG,"Notification Sent $title, $message")
    }
}