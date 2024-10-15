package com.carrental.carspeeddemo.model

/**
 * Interface for speed change observation“.
 */
interface ISpeedChangeListener {
    /**
     * Speed change event.
     */
    fun onSpeedChange(currentSpeed: Int)
}
