package com.carrental.carspeeddemo.model

import android.content.Context


interface CarPropertyUseCase {

    /**
     * Init Car Property Manager.
     */
    fun initiateCarPropertyManager(context: Context)

    /**
     * Start observing car speed change event.
     */
    fun startObservingCarSpeed(listener: ISpeedChangeListener)

    /**
     *  Register call back.
     */
    fun registerPropertyChangeCallBack()
}