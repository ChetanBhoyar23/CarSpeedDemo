package com.carrental.carspeeddemo.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carrental.carspeeddemo.model.ISpeedChangeListener
import com.carrental.carspeeddemo.repository.CarSpeedRepository
import com.carrental.carspeeddemo.utils.ApplicationDataHandler
import com.carrental.carspeeddemo.utils.RentalCarType
import kotlinx.coroutines.launch

/**
 *  Activity View Model for MainCar activity.
 */
class MainCarViewModel(
    private val repository: CarSpeedRepository,
    private val applicationDataHandler: ApplicationDataHandler
) : ViewModel(), ISpeedChangeListener {

    companion object {
        private const val TAG: String = "SpeedViewModel"
    }

    private var maxSpeedLimit: Int? = 0
    val speedLiveData = MutableLiveData<Int>()
    val speedLimitExceededLiveData = MutableLiveData<Boolean>()

    /**
     * Get default Speed for rental car group.
     */
    fun getDefaultSpeed(carId: String, fleetId: String) {
        viewModelScope.launch {
            val defaultSpeedLimit = repository.getDefaultSpeed(carId, fleetId)
            Log.d(TAG, "Default Speed: $defaultSpeedLimit")
        }
    }

    /**
     * Get max speed limit for Car.
     */
    fun getMaxSpeedLimit(carId: String) {
        viewModelScope.launch {
            maxSpeedLimit = repository.getSpeedLimitForCar(carId)
            Log.d(TAG, "Max Speed: $maxSpeedLimit")
        }
    }

    /**
     * Set max speed limit for car.
     */
    fun setMaxSpeedLimit(carId: String, maxSpeed: Int) {
        viewModelScope.launch {
            repository.setSpeedLimitForCar(carId, maxSpeed)
        }
    }

    override fun onSpeedChange(currentSpeed: Int) {
        val maxSpeed: Int = maxSpeedLimit ?: 0
        speedLiveData.postValue(currentSpeed)
        Log.d(TAG, "Speed Limit: $maxSpeed , Car Speed:$currentSpeed")
        if (currentSpeed > maxSpeed) {
            speedLimitExceededLiveData.postValue(true)
            val carId: String = applicationDataHandler.getCarData(RentalCarType.CAR_ID.name)

            // Sending notification
            repository.sendNotificationToCompany(
                title = "Speed Limit Exceeded.",
                message = "Car : $carId has exceeded the speed limit.",
                carId = carId
            )
        } else {
            speedLimitExceededLiveData.postValue(false)
        }
    }
}