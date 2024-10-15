package com.carrental.carspeeddemo

import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.carrental.carspeeddemo.manager.NotificationManager
import com.carrental.carspeeddemo.model.ISpeedChangeListener
import com.carrental.carspeeddemo.model.LocationService
import com.carrental.carspeeddemo.receiver.SpeedReceiver
import com.carrental.carspeeddemo.repository.CarSpeedRepository
import com.carrental.carspeeddemo.utils.ApplicationDataHandler
import com.carrental.carspeeddemo.utils.Constants
import com.carrental.carspeeddemo.utils.NotificationsUtil
import com.carrental.carspeeddemo.utils.RentalCarType
import com.carrental.carspeeddemo.viewmodel.MainCarViewModel

/**
 * This is main car activity, visible to driver. It is responsible for showing notification,
 * manage car data, speed data through view model.
 */
class MainCarActivity : AppCompatActivity() {

    companion object {
        private const val TAG: String = "MainCarActivity"
    }

    private var applicationDataHandler: ApplicationDataHandler = ApplicationDataHandler()

    private var notificationManager: NotificationManager = NotificationManager()

    private var carSpeedRepository: CarSpeedRepository =
        CarSpeedRepository(applicationDataHandler, notificationManager)

    private var mainCarViewModel: MainCarViewModel =
        MainCarViewModel(carSpeedRepository, applicationDataHandler)

    private var speedReceiver: SpeedReceiver? = null

    // Speed listener
    private val speedListener: ISpeedChangeListener = mainCarViewModel

    private var textView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialise notification firebase manager or AWS manger by passing true/false.
        // This will determine by which service is live now.
        // Assumption: Firebase notification is ON now.
        notificationManager.initNotificationManager(true)
        initViews()
        initialDataSetup()
        initObservers()
    }

    private fun initViews() {
        textView = findViewById<TextView>(R.id.currentSpeedTextView)
    }

    private fun initialDataSetup() {
        // Set car id and fleet id/ group id.
        applicationDataHandler.setCarData(RentalCarType.CAR_ID, Constants.CAR_ID)

        // Rental company should sets default speed limit for rental car group
        mainCarViewModel.getDefaultSpeed(Constants.CAR_ID)

        // Rental agent sets a specific speed limit for a car.
        mainCarViewModel.setMaxSpeedLimit(Constants.CAR_ID, Constants.MAX_SPEED)

        // Get speed limit set for a car by agent.
        mainCarViewModel.getMaxSpeedLimit(Constants.CAR_ID)
    }

    private fun initObservers() {
        // Observe the speed change.
        mainCarViewModel.speedLiveData.observe(this) { speed ->
            // Update current car speed to UI
            textView?.text = "Car Speed: $speed"
        }

        // Show speed limit exceed warning pop up.
        mainCarViewModel.speedLimitExceededLiveData.observe(this) { exceeded ->
            if (exceeded) {
                showAlert()
            }
        }
    }


    override fun onStart() {
        super.onStart()
        registerReceiver()
        startLocationService()
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun registerReceiver() {
        // Register BroadcastReceiver
        speedReceiver = SpeedReceiver(speedListener)
        registerReceiver(speedReceiver, IntentFilter(Constants.ACTION_ID), RECEIVER_NOT_EXPORTED)
    }

    private fun startLocationService() {
        // Start the Location Service.
        val serviceIntent = Intent(this, LocationService::class.java)
        startService(serviceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "On Destroy")
        // un-register receiver.
        unregisterReceiver(speedReceiver)
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