package com.elslode.weather.data.modelWeather

data class HourlyDto(
    val time: String? = null,
    val tempC: String? = null,
    val tempF: String? = null,
    val windspeedMiles: String? = null,
    val windspeedKmph: String? = null,
    val weatherIconUrl: List<WeatherIconUrlDto>? = null,
    val weatherDesc: List<WeatherDescDto>? = null,
    val weather_ru: List<WeatherDescDto>? = null,
    val feelsLikeC: String? = null,
    val feelsLikeF: String? = null
)
