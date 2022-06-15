package com.elslode.weather.domain.entityModel

data class WeatherEntity(
    val id: Int? = null,
    val date: String? = null,
    val astronomy: List<Astronomy>? = null,
    val maxtempC: String? = null,
    val maxtempF: String? = null,
    val mintempC: String? = null,
    val mintempF: String? = null,
    val avgtempC: String? = null,
    val avgtempF: String? = null,
    val sunHour: String? = null,
    val uvIndex: String? = null,
    val hourly: List<Hourly>? = null
)
