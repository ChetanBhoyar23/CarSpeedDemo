package com.carrental.carspeeddemo.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.carrental.carspeeddemo.MyApplication
import com.carrental.carspeeddemo.utils.ApplicationDataHandler
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: MyApplication) {

    @Singleton
    @Provides
    fun provideApplicationContext(): Context = application

    @Singleton
    @Provides
    fun provideApplicationDataHandler(): ApplicationDataHandler = ApplicationDataHandler()
}