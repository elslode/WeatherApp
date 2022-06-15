package com.elslode.weather.data.modelWeather

data class CurrentConditionDto(
    val temp_F: String? = null,
    val temp_C: String? = null,
    val weatherIconUrl: List<WeatherIconUrlDto>? = null,
    val weatherDesc: List<WeatherDescDto>? = null,
    val windspeedMiles: String? = null,
    val windspeedKmph: String? = null,
    val FeelsLikeC: String? = null,
    val FeelsLikeF: String? = null
)