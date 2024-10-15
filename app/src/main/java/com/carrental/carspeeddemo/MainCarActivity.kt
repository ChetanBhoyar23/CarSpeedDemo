package com.carrental.carspeeddemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.carrental.carspeeddemo.manager.NotificationManager
import com.carrental.carspeeddemo.model.LocationService
import com.carrental.carspeeddemo.utils.ApplicationDataHandler
import com.carrental.carspeeddemo.utils.Constants
import com.carrental.carspeeddemo.utils.NotificationsUtil
import com.carrental.carspeeddemo.utils.PreferenceType
import com.carrental.carspeeddemo.viewmodel.SpeedViewModel
import javax.inject.Inject

/**
 * This is main car activity, visible to driver. It is responsible for showing notification,
 * manage car data, speed data through view model.
 */
class MainCarActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "MainCarActivity"
    }

    @Inject
    lateinit var speedViewModel: SpeedViewModel

    @Inject
    lateinit var applicationDataHandler: ApplicationDataHandler

    @Inject
    lateinit var notificationManager: NotificationManager

    //private lateinit var activityBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as MyApplication).applicationComponent.inject(this)


        // Set car id and fleet id/ group id.
        applicationDataHandler.setCarData(PreferenceType.CAR_ID, Constants.CAR_ID)
        applicationDataHandler.setCarData(PreferenceType.FLEET_ID, Constants.FLEET_ID)

        // Initialise notification firebase manager.
        notificationManager.initFirebaseManager()

        // Rental company can sets default speed limit for rental car group
        speedViewModel.getDefaultSpeedLimit(Constants.CAR_ID, Constants.FLEET_ID)


        // Rental agent sets a specific speed limit for a car.
        speedViewModel.setSpeedLimitForCar(Constants.CAR_ID, Constants.MAX_SPEED)


        // Get speed limit set for a car by agent.
        speedViewModel.getSpeedLimitForCar(Constants.CAR_ID)


        // Observe the speed change and update UI.
        speedViewModel.speedLiveData.observe(this) { speed ->
            // Update UI with current speed
            findViewById<TextView>(R.id.speedTextView).text = "Speed: $speed km/h"
        }

        // Show speed limit exceed warning pop up.
        speedViewModel.speedLimitExceededLiveData.observe(this) { exceeded ->
            if (exceeded) {
                showAlert()
            }
        }

        // Handle error case.
        speedViewModel.errorLiveData.observe(this) { error ->
            // Handle error
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }

        // Start the Location Service.
        Intent(this, LocationService::class.java).also {
            startService(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "On Destroy")
        // Stop service.
        Intent(this, LocationService::class.java).also {
            stopService(it)
        }
    }

    private fun showAlert() {
        // on screen notification.
        NotificationsUtil.buildNotification(
            context = this,
            title = getString(R.string.speed_alert_title),
            description = getString(R.string.speed_alert_message),
        )
    }
}