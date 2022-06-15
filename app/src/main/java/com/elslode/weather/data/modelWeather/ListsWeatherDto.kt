package com.elslode.weather.data.modelWeather

data class ListsWeatherDto(
    val request: List<RequestDto>? = null,
    val current_condition: List<CurrentConditionDto>? = null,
    val weather: List<WeatherDto>? = null
    // TODO:  Если нужны будут данные по средним значениям за год по месяцам,
    //  но это уже на интерес расширения приложения
//    val climateAverages: List<ClimateAverage>? = null
)