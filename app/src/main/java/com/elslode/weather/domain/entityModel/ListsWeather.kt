package com.elslode.weather.domain.entityModel

data class ListsWeather(
    val request: List<Request>? = null,
    val current_condition: List<CurrentCondition>? = null,
    val weather: List<Weather>? = null,
    // TODO:  Если нужны будут данные по средним значениям за год по месяцам,
    //  но это уже на интерес расширения приложения
//    val climateAverages: List<ClimateAverage>? = null
)