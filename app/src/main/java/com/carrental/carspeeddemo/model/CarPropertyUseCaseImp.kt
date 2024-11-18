package com.carrental.carspeeddemo.model

import android.content.Context
import android.util.Log
import java.lang.String
import kotlin.Int


/**
 * Dummy implementation for Car Property Manager.
 */
class CarPropertyUseCaseImp : CarPropertyUseCase {

   // private val carPropertyManager: CarPropertyManager? = null
    private var speedListener: ISpeedChangeListener? = null


    override fun initiateCarPropertyManager(context: Context) {
       // Initiate CarPropertyManager here
       // carPropertyManager = (CarPropertyManager) Car.createCar(context).getCarManager(Car.PROPERTY_SERVICE);
    }

    override fun startObservingCarSpeed(listener: ISpeedChangeListener) {
        // Receive Speed change event.
        speedListener = listener
    }

    override fun registerPropertyChangeCallBack() {
       // Register property change call back.
        /*carPropertyManager.registerCallback(object : CarPropertyEventCallback() {
            override fun onChangeEvent(carPropertyValue: CarPropertyValue) {
                Log.d(
                    TAG,
                    ("PERF_VEHICLE_SPEED: onChangeEvent(" + carPropertyValue.getValue()).toString() + ")"
                )
                speedListener.onSpeedChange(carPropertyValue.getValue())
            }

            override fun onErrorEvent(propId: Int, zone: Int) {
                Log.d(TAG, "PERF_VEHICLE_SPEED: onErrorEvent($propId, $zone)")
            }
        }, VehiclePropertyIds.PERF_VEHICLE_SPEED, CarPropertyManager.SENSOR_RATE_NORMAL)*/
    }
}