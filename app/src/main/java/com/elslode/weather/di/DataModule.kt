package com.elslode.weather.di

import android.app.Application
import android.content.Context
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

        @Provides
        fun provideContext(application: Application): Context {
            return application.applicationContext
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