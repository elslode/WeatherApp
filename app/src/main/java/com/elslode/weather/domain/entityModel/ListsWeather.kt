package com.elslode.weather.domain.entityModel

data class ListsWeather(
    val request: List<Request>? = null,
    val current_condition: List<CurrentCondition>? = null,
    val weather: List<WeatherEntity>? = null,
)