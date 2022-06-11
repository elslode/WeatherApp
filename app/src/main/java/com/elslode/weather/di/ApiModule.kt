package com.elslode.weather.di

import com.elslode.weather.data.network.ApiWeather
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
interface ApiModule {

    companion object {
        @Provides
        @Singleton
        fun provideRestAdapter(): ApiWeather =
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiWeather.BASE_URL)
                .build()
                .create(ApiWeather::class.java)
    }
}