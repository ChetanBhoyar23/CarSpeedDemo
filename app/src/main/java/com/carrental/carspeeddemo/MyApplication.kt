package com.carrental.carspeeddemo

import android.app.Application
import com.carrental.carspeeddemo.di.AppModule
import com.carrental.carspeeddemo.di.ApplicationComponent
import com.carrental.carspeeddemo.di.DaggerApplicationComponent
import com.carrental.carspeeddemo.manager.NotificationManager
import javax.inject.Inject

/**
 * Application class.
 */
class MyApplication : Application() {

    @Inject
    lateinit var notificationManager: NotificationManager

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        initDagger()
    }

    private fun initDagger() {
        applicationComponent =
            DaggerApplicationComponent.builder().appModule(AppModule(this)).build()
    }
}