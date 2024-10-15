package com.carrental.carspeeddemo.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carrental.carspeeddemo.model.SpeedLimit
import com.carrental.carspeeddemo.repository.CarSpeedRepository
import com.carrental.carspeeddemo.utils.ApplicationDataHandler
import com.carrental.carspeeddemo.utils.RentalCarType
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpeedViewModel @Inject constructor(private val repository: CarSpeedRepository) : ViewModel() {

    companion object {
        private const val TAG: String = "SpeedViewModel"
    }

    @Inject
    lateinit var applicationDataHandler: ApplicationDataHandler

    private var speedLimit: SpeedLimit? = null
    val speedLiveData = MutableLiveData<Int>()
    val speedLimitExceededLiveData = MutableLiveData<Boolean>()
    val errorLiveData = MutableLiveData<String>()

    fun checkSpeed(currentSpeed: Int) {
        val maxSpeed: Int = speedLimit?.maxSpeed ?: 0
        speedLiveData.postValue(currentSpeed)
        Log.d(TAG,"Speed Limit: $maxSpeed , Car Speed:$currentSpeed")
        if (currentSpeed > maxSpeed) {
            speedLimitExceededLiveData.postValue(true)
            val carId:String = applicationDataHandler.getCarData(RentalCarType.CAR_ID.name)

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

    fun getDefaultSpeedLimit(carId:String, fleetId: String) {
        viewModelScope.launch {
            val defaultSpeedLimit = repository.getDefaultSpeedLimit(carId, fleetId)
            Log.d(TAG,"Default Speed: $defaultSpeedLimit")
        }
    }

    fun getSpeedLimitForCar(carId: String) {
        viewModelScope.launch {
            speedLimit = repository.getSpeedLimitForCar(carId)
            Log.d(TAG,"Max Speed: $speedLimit")
        }
    }

    fun setSpeedLimitForCar(carId: String, maxSpeed: Int) {
        viewModelScope.launch {
            repository.setSpeedLimitForCar(SpeedLimit(carId, maxSpeed))
        }
    }
}
