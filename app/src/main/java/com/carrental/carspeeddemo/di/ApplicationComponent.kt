package com.carrental.carspeeddemo.di

import com.carrental.carspeeddemo.MainCarActivity
import com.carrental.carspeeddemo.model.LocationService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface ApplicationComponent {
    fun inject(mainCarActivity: MainCarActivity)
    fun inject(service : LocationService)
}