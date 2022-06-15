package com.elslode.weather.data.network

import com.elslode.weather.data.modelWeather.ResponseDataDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiWeather {

    @GET("premium/v1/weather.ashx")
   suspend fun getWeather(
        @Query(KEY_API) key: String = APIKEY,
        @Query(Q_KEY) q: String,
        @Query(DAYS_KEY) num_of_days: Int = 7,
        @Query(FORMAT_KEY) format: String = JSON,
        @Query(INTERVAL_TIME) tp: Int = 24
    ): ResponseDataDto

    companion object {
        const val BASE_URL = "https://api.worldweatheronline.com/"
        private const val KEY_API = "key"
        private const val DAYS_KEY = "num_of_days"
        private const val FORMAT_KEY = "format"
        private const val INTERVAL_TIME = "tp"
        private const val APIKEY = "3119850ca0b641fe80f212423221206"
        private const val Q_KEY = "q"
        private const val JSON = "json"
    }
}