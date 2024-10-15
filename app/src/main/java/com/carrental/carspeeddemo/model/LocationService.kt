package com.carrental.carspeeddemo.model

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.ServiceCompat
import com.carrental.carspeeddemo.model.LocationService.Constant.MIN_DISTANCE
import com.carrental.carspeeddemo.model.LocationService.Constant.MIN_TIME
import com.carrental.carspeeddemo.model.LocationService.Constant.TAG
import com.carrental.carspeeddemo.utils.Constants
import com.carrental.carspeeddemo.utils.Constants.SPEED_DATA
import com.carrental.carspeeddemo.utils.NotificationsUtil

/**
 * This is location service, it will notify the user when location got changed.
 */
class LocationService: Service() {

    object Constant {
        // TAG
        const val TAG: String = "LocationService"
        const val MIN_DISTANCE: Float = 1f
        const val MIN_TIME: Long = 1000
    }

    private lateinit var locationManager: LocationManager

    private val locationListener = LocationListenerImpl()

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.d(TAG, "onCreate")
       // (application as MyApplication).applicationComponent.inject(this)

        //For mobile.
        startForegroundService()

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        super.onCreate()
    }

    private fun startForegroundService() {
        NotificationsUtil.getNotificationManager(this)
            .createNotificationChannel(NotificationsUtil.createNotificationChannel())

        val ID = 11011
        ServiceCompat.startForeground(
            this,
            ID,
            NotificationsUtil.buildForegroundNotification(
                this,
                "Car Speed",
                "Car speed under surveillance",
                true
            ),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startLocationUpdates()
        return START_STICKY
    }

    private fun startLocationUpdates() {
        try {
            Log.d(TAG, "Service started to monitor location change")
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME,
                MIN_DISTANCE,
                locationListener
            )
        } catch (e: SecurityException) {
            // Error handling
            e.printStackTrace()
        }
    }

    // Location Change Listener
    private inner class LocationListenerImpl : LocationListener {
        @SuppressLint("UnsafeImplicitIntentLaunch")
        override fun onLocationChanged(location: Location) {
            Log.d(TAG, "current speed = ${location.speed}")
            val speed: Double = location.speed.toDouble()

            // Send Speed change data over broad cast
            val intent = Intent(Constants.ACTION_ID)
            intent.putExtra(SPEED_DATA, speed)
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
            sendBroadcast(intent)
            Log.d(TAG, "Speed change broadcast sent.")
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        // Remove / dispose listener
        locationManager.removeUpdates(locationListener)
    }
}