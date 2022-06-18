package com.elslode.weather.domain.repos

import com.elslode.weather.domain.entityModel.ResponseDataEntity

interface Repository {
     suspend fun getWeather(lat_lon: String): ResponseDataEntity
    suspend fun getWeatherForDataDetail(q: String, dataTime: String): ResponseDataEntity
}