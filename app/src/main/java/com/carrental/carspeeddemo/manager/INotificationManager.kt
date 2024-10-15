package com.carrental.carspeeddemo.manager

/**
 * This is Notification Manager class.
 */
interface INotificationManager {

    // Send Notification.
    fun sendNotification(title: String, message: String, carId: String)
}
