package com.elslode.weather.data.modelWeather

data class WeatherDto(
    val id: Int? = null,
    val date: String? = null,
    val astronomy: List<AstronomyDto>? = null,
    val maxtempC: String? = null,
    val maxtempF: String? = null,
    val mintempC: String? = null,
    val mintempF: String? = null,
    val avgtempC: String? = null,
    val avgtempF: String? = null,
    val sunHour: String? = null,
    val uvIndex: String? = null,
    val hourly: List<HourlyDto>? = null
)

