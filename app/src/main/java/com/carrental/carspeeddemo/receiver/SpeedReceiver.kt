package com.carrental.carspeeddemo.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.carrental.carspeeddemo.model.ISpeedChangeListener
import com.carrental.carspeeddemo.utils.Constants.SPEED_DATA

/**
 * Speed change receiver.
 */
class SpeedReceiver(private val speedListener: ISpeedChangeListener) : BroadcastReceiver() {
    private val defaultLimit = 10
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("TAG", "Speed change broadcast received.")
            val changeSpeed = intent.getIntExtra(SPEED_DATA, defaultLimit)
            speedListener.onSpeedChange(changeSpeed)
        }
}