package com.elslode.weather

import android.app.Application
import com.elslode.weather.di.DaggerApplicationComponent

class WeatherApp: Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

}