package com.carrental.carspeeddemo.repository

import com.carrental.carspeeddemo.model.DefaultSpeedLimit
import com.carrental.carspeeddemo.model.SpeedLimit

interface ISpeedRepository {
    suspend fun getDefaultSpeedLimit(carId: String, fleetId: String): DefaultSpeedLimit?
    suspend fun getSpeedLimitForCar(carId: String): SpeedLimit?
    suspend fun setSpeedLimitForCar(speedLimit: SpeedLimit)
}