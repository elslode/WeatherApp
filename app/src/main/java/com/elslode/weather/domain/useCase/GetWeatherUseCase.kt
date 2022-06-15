package com.elslode.weather.domain.useCase

import com.elslode.weather.domain.repos.Repository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(lat_lon: String) = repository.getWeather(lat_lon)
}