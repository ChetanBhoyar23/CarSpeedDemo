package com.carrental.carspeeddemo.repository

import com.carrental.carspeeddemo.manager.NotificationManager
import com.carrental.carspeeddemo.model.DefaultSpeedLimit
import com.carrental.carspeeddemo.model.SpeedLimit
import com.carrental.carspeeddemo.utils.ApplicationDataHandler
import com.carrental.carspeeddemo.utils.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class CarSpeedRepository @Inject constructor() : ISpeedRepository {

    @Inject
    lateinit var applicationDataHandler: ApplicationDataHandler

    @Inject
    lateinit var notificationManager: NotificationManager


    // Send a notification for over speed car.
    fun sendNotificationToCompany(title: String, message: String, carId: String) {
        notificationManager.sendNotification(title, message, carId)
    }

// Get default speed for rental car group.
    override suspend fun getDefaultSpeedLimit(carId: String, fleetId: String): DefaultSpeedLimit? {
        applicationDataHandler.setCarSpeed(carId, Constants.DEFAULT_MAX_SPEED)
        return applicationDataHandler.getCarSpeed(carId)?.let {
            DefaultSpeedLimit(
                fleetId,
                it
            )
        }
    }

    //  Get speed limit for rental car group.
    override suspend fun getSpeedLimitForCar(carId: String): SpeedLimit? {
        val maxSpeed = applicationDataHandler.getCarSpeed(carId)
        return (if (maxSpeed == 0) applicationDataHandler.getCarSpeed(carId) else maxSpeed)?.let {
            SpeedLimit(
                carId,
                it
            )
        }
    }

    override suspend fun setSpeedLimitForCar(speedLimit: SpeedLimit) {
        applicationDataHandler.setCarSpeed(speedLimit.carId, speedLimit.maxSpeed)
    }
}
