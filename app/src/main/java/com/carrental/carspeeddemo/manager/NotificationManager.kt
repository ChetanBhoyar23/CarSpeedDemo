package com.carrental.carspeeddemo.manager

import javax.inject.Inject
import javax.inject.Singleton

/**
 * This is notification manager, manage to send notification.
 */
@Singleton
class NotificationManager @Inject constructor() {

    private var notificationManager: INotificationManager? = null

    // Firebase notification manager.
    fun initFirebaseManager() {
        notificationManager = FirebaseNotificationManagerManager()
    }

    // AWS notification manager.
    fun initAWSManager() {
        notificationManager = AWSNotificationManagerManager()
    }

    // Send notification via manager.
    fun sendNotification(title: String, message: String, carId: String){
        notificationManager?.sendNotification(title,message,carId)
    }
}