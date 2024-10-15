package com.carrental.carspeeddemo.manager

interface INotificationManager {

    fun sendNotification(title: String, message: String, carId: String)
}
