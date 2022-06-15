package com.elslode.weather.data.repo

import com.elslode.weather.data.mapper.WeatherMapper
import com.elslode.weather.data.network.ApiWeather
import com.elslode.weather.domain.entityModel.ResponseData
import com.elslode.weather.domain.repos.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiWeather: ApiWeather,
    private val mapper: WeatherMapper
) : Repository {

    override suspend fun getWeather(lat_lon: String): ResponseData =
        mapper.mapDataDtoToDataEntity(apiWeather.getWeather(q = lat_lon))

}