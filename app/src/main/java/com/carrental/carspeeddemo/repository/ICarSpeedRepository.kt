package com.carrental.carspeeddemo.repository

/**
 * Interface for Car repository.
 */
interface ICarSpeedRepository {
    suspend fun getDefaultSpeed(carId: String): Int?
    suspend fun getSpeedLimitForCar(carId: String): Int?
    suspend fun setSpeedLimitForCar(carId: String, maxSpeed: Int)
}