package com.elslode.weather.domain.entityModel

data class CurrentCondition(
    val temp_F: String? = null,
    val temp_C: String? = null,
    val weatherIconUrl: List<WeatherIconUrl>? = null,
    val weatherDesc: List<WeatherDesc>? = null,
    val windspeedMiles: String? = null,
    val windspeedKmph: String? = null,
    val FeelsLikeC: String? = null,
    val FeelsLikeF: String? = null
)