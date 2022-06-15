package com.elslode.weather.data.mapper

import com.elslode.weather.data.modelWeather.*
import com.elslode.weather.domain.entityModel.*
import javax.inject.Inject

class WeatherMapper @Inject constructor() {

    fun mapDataDtoToDataEntity(dataDto: ResponseDataDto) =
        ResponseData(
            data = dataDto.data?.let { mapWeatherResponseDtoToWeatherEntity(it) }
        )

    private fun mapWeatherResponseDtoToWeatherEntity(weather: ListsWeatherDto) =
        ListsWeather(
            weather = weather.weather?.map {
                mapWeatherDtoToWeatherEntity(it)
            },
            current_condition = weather.current_condition?.map {
                mapCurrentConditionDtoToCurrentConditionEntity(it)
            },
            request = weather.request?.map {
                mapRequestDtoToRequestEntity(it)
            }
        )

    private fun mapRequestDtoToRequestEntity(requestDto: RequestDto) =
        Request(
            type = requestDto.type,
            query = requestDto.query
        )

    private fun mapWeatherDtoToWeatherEntity(weatherDto: WeatherDto) =
        Weather(
            date = weatherDto.date,
            maxtempC = weatherDto.maxtempC,
            maxtempF = weatherDto.maxtempF,
            mintempC = weatherDto.mintempC,
            mintempF = weatherDto.mintempF,
            astronomy = weatherDto.astronomy?.map {
                mapAstronomyDtoToAstronomyEntity(it)
            },
            avgtempC = weatherDto.avgtempC,
            avgtempF = weatherDto.avgtempF,
            sunHour = weatherDto.sunHour,
            uvIndex = weatherDto.uvIndex,
            hourly = weatherDto.hourly?.map {
                mapHourlyDtoToHourlyEntity(it)
            }
        )

    private fun mapAstronomyDtoToAstronomyEntity(astronomyDto: AstronomyDto) =
        Astronomy(
            sunrise = astronomyDto.sunrise,
            sunset = astronomyDto.sunset,
            moonrise = astronomyDto.moonrise,
            moonset = astronomyDto.moonset,
            moonPhase = astronomyDto.moonPhase,
            moonIllumination = astronomyDto.moonIllumination
        )

    private fun mapHourlyDtoToHourlyEntity(hourlyDto: HourlyDto) =
        Hourly(
            time = hourlyDto.time,
            tempC = hourlyDto.tempC,
            tempF = hourlyDto.tempF,
            weatherDesc = hourlyDto.weatherDesc?.map {
                mapWeatherDescDtoToWeatherEntity(it)
            },
            weatherIconUrl = hourlyDto.weatherIconUrl?.map {
                mapWeatherIconUrlToWeatherIconEntity(it)
            },
            windspeedKmph = hourlyDto.windspeedKmph,
            windspeedMiles = hourlyDto.windspeedMiles,
            feelsLikeC = hourlyDto.feelsLikeC,
            feelsLikeF = hourlyDto.feelsLikeF
        )

    private fun mapCurrentConditionDtoToCurrentConditionEntity(currentConditionDto: CurrentConditionDto) =
        CurrentCondition(
            temp_C = currentConditionDto.temp_C,
            temp_F = currentConditionDto.temp_F,
            weatherIconUrl = currentConditionDto.weatherIconUrl?.map {
                mapWeatherIconUrlToWeatherIconEntity(it)
            },
            weatherDesc = currentConditionDto.weatherDesc?.map {
                mapWeatherDescDtoToWeatherEntity(it)
            },
            windspeedMiles = currentConditionDto.windspeedMiles,
            windspeedKmph = currentConditionDto.windspeedKmph,
            FeelsLikeC = currentConditionDto.FeelsLikeF,
            FeelsLikeF = currentConditionDto.FeelsLikeC
        )

    private fun mapWeatherDescDtoToWeatherEntity(weatherDescDto: WeatherDescDto) =
        WeatherDesc(
            value = weatherDescDto.value
        )

    private fun mapWeatherIconUrlToWeatherIconEntity(weatherIconUrlDto: WeatherIconUrlDto) =
        WeatherIconUrl(
            value = weatherIconUrlDto.value
        )
}