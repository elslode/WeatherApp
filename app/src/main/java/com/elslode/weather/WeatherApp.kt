package com.elslode.weather

import android.app.Application
import android.content.Context
import com.elslode.weather.di.ApplicationComponent
import com.elslode.weather.di.DaggerApplicationComponent

class WeatherApp: Application() {

    companion object {
        lateinit var instance: WeatherApp
    }

    private lateinit var appComponent: ApplicationComponent

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        instance = this
        component
    }

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

}