package com.elslode.weather.domain.repos

import com.elslode.weather.domain.entityModel.ResponseData

interface Repository {
    suspend fun getWeather(lat_lon: String): ResponseData
}