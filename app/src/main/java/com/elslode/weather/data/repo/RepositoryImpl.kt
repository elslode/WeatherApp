package com.elslode.weather.data.repo

import com.elslode.weather.data.mapper.WeatherMapper
import com.elslode.weather.data.network.ApiWeather
import com.elslode.weather.domain.entityModel.ResponseDataEntity
import com.elslode.weather.domain.repos.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiWeather: ApiWeather,
    private val mapper: WeatherMapper
) : Repository {

    override suspend fun getWeather(lat_lon: String): ResponseDataEntity =
        mapper.mapDataDtoToDataEntity(apiWeather.getWeather(q = lat_lon))

    override suspend fun getWeatherForDataDetail(query: String, dataTime: String): ResponseDataEntity =
        mapper.mapDataDtoToDataEntity(apiWeather.getWeatherForDetail(q = query, data = dataTime))
}