package com.elslode.weather.data.modelWeather

data class ListsWeatherDto(
    val request: List<RequestDto>? = null,
    val current_condition: List<CurrentConditionDto>? = null,
    val weather: List<WeatherDto>? = null
)