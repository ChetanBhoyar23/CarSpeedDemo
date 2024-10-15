package com.carrental.carspeeddemo.repository

import com.carrental.carspeeddemo.manager.NotificationManager
import com.carrental.carspeeddemo.utils.ApplicationDataHandler
import com.carrental.carspeeddemo.utils.Constants

/**
 * Car repository class, responsible for managing data.
 */
open class CarSpeedRepository(
    val applicationDataHandler: ApplicationDataHandler,
    val notificationManager: NotificationManager
) : ICarSpeedRepository {

    // Send a notification for over speed car.
    fun sendNotificationToCompany(title: String, message: String, carId: String) {
        notificationManager.sendNotification(title, message, carId)
    }

    // Get default speed for rental car group.
    override suspend fun getDefaultSpeed(carId: String, fleetId: String): Int? {
        applicationDataHandler.setCarSpeed(carId, Constants.DEFAULT_MAX_SPEED)
        return applicationDataHandler.getCarSpeed(carId).let {
            it
        }
    }

    //  Get speed limit for rental car group.
    override suspend fun getSpeedLimitForCar(carId: String): Int? {
        val maxSpeed = applicationDataHandler.getCarSpeed(carId)
        return if (maxSpeed == 0) applicationDataHandler.getCarSpeed(carId) else maxSpeed
    }

    // Sets Speed limit.
    override suspend fun setSpeedLimitForCar(carId: String, maxSpeed: Int) {
        applicationDataHandler.setCarSpeed(carId, maxSpeed)
    }
}