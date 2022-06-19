package com.elslode.weather.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.elslode.weather.data.network.ApiWeather
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
interface DataModule {

    companion object {

        private const val PREFS_NAME = "weatherApp_preferences"

        @Provides
        fun provideContext(application: Application): Context {
            return application.applicationContext
        }

        @Singleton
        @Provides
        fun provideSharedPreferences(application: Application): SharedPreferences {
            return application.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }

        @Provides
        @Singleton
        fun provideRetrofitApi(): ApiWeather {
            return Retrofit.Builder()
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiWeather.BASE_URL)
                .build()
                .create(ApiWeather::class.java)
        }
    }
}