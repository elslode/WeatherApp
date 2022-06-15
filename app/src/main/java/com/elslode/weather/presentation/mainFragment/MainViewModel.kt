package com.elslode.weather.presentation.mainFragment

import androidx.lifecycle.ViewModel
import com.elslode.weather.data.repo.RepositoryImpl
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repositoryImpl: RepositoryImpl
) : ViewModel() {

    suspend fun getWeather(lat_lon: String) = repositoryImpl.getWeather(lat_lon)
}