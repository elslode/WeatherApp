package com.elslode.weather.domain.entityModel

data class Hourly(
    val id: Int = 0,
    val time: String? = null,
    val tempC: String? = null,
    val tempF: String? = null,
    val windspeedMiles: String? = null,
    val windspeedKmph: String? = null,
    val weatherIconUrl: List<WeatherIconUrl>? = null,
    val weatherDesc: List<WeatherDesc>? = null,
    val weather_ru: List<WeatherDesc>? = null,
)
