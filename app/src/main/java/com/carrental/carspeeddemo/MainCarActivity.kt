package com.carrental.carspeeddemo

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.carrental.carspeeddemo.manager.NotificationManager
import com.carrental.carspeeddemo.model.ISpeedChangeListener
import com.carrental.carspeeddemo.model.LocationService
import com.carrental.carspeeddemo.model.LocationService.Constant
import com.carrental.carspeeddemo.receiver.SpeedReceiver
import com.carrental.carspeeddemo.repository.CarSpeedRepository
import com.carrental.carspeeddemo.utils.ApplicationDataHandler
import com.carrental.carspeeddemo.utils.Constants
import com.carrental.carspeeddemo.utils.NotificationsUtil
import com.carrental.carspeeddemo.utils.RentalCarType
import com.carrental.carspeeddemo.viewmodel.SpeedViewModel

/**
 * This is main car activity, visible to driver. It is responsible for showing notification,
 * manage car data, speed data through view model.
 */
class MainCarActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "MainCarActivity"
    }

    var part1: String = "name"
    var part2: String = "age"

    private var applicationDataHandler: ApplicationDataHandler = ApplicationDataHandler()

    private var notificationManager: NotificationManager = NotificationManager()

    private var carSpeedRepository: CarSpeedRepository =
        CarSpeedRepository(applicationDataHandler, notificationManager)

    private var speedViewModel: SpeedViewModel =
        SpeedViewModel(carSpeedRepository, applicationDataHandler)

    private var speedReceiver: SpeedReceiver? = null

    var ACTION_ID: String = "com.alex.receivers.id1"

    private val speedListener: ISpeedChangeListener = speedViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialise notification firebase manager.
        notificationManager.initFirebaseManager()
        initialDataSetup()
        initObservers()
        registerReceiver()
        startLocationService()
    }

    private fun initialDataSetup() {
        // Set car id and fleet id/ group id.
        applicationDataHandler.setCarData(RentalCarType.CAR_ID, Constants.CAR_ID)
        applicationDataHandler.setCarData(RentalCarType.FLEET_ID, Constants.FLEET_ID)

        // Rental company can sets default speed limit for rental car group
        speedViewModel.getDefaultSpeedLimit(Constants.CAR_ID, Constants.FLEET_ID)

        // Rental agent sets a specific speed limit for a car.
        speedViewModel.setSpeedLimitForCar(Constants.CAR_ID, Constants.MAX_SPEED)

        // Get speed limit set for a car by agent.
        speedViewModel.getSpeedLimitForCar(Constants.CAR_ID)
    }

    private fun initObservers() {
        // Observe the speed change.
        speedViewModel.speedLiveData.observe(this) { speed ->
            // Update current car speed
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
    }

    override fun onStart() {
        super.onStart()

    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun registerReceiver() {
        // Register BroadcastReceiver
        speedReceiver = SpeedReceiver(speedListener)
        registerReceiver(speedReceiver, IntentFilter(ACTION_ID), RECEIVER_NOT_EXPORTED)
    }

    private fun startLocationService() {
        // Start the Location Service.
        val serviceIntent = Intent(this, LocationService::class.java)
        startService(serviceIntent)
    }

    override fun onStop() {
        // un-register receiver.
        unregisterReceiver(speedReceiver)
        super.onStop()
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