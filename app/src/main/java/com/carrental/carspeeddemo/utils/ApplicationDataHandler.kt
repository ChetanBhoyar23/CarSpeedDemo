package com.carrental.carspeeddemo.utils

/**
 * Data handler class, responsible to handle data.
 */
class ApplicationDataHandler {

    // Car data
    private var carDataHashMap : HashMap<String, String> = HashMap<String, String> ()
    // Speed data
    private var speedDataHashMap : HashMap<String, Int> = HashMap<String, Int> ()

    fun setCarData(key: RentalCarType, value: String){
        carDataHashMap.put(key.toString(), value)
    }

    fun getCarData(key: String): String {
        return carDataHashMap.get(key) ?: "DEFAULT"
    }

    fun setCarSpeed(key: String, value: Int) {
        speedDataHashMap.put(key, value)
    }

    fun getCarSpeed(key: String): Int {
       return speedDataHashMap.get(key) ?: 0
    }
}