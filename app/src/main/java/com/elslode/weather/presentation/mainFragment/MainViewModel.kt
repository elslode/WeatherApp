package com.elslode.weather.presentation.mainFragment

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elslode.weather.data.sharedPref.HelperPreferences.clearCity
import com.elslode.weather.data.sharedPref.HelperPreferences.put
import com.elslode.weather.data.sharedPref.PrefKeys
import com.elslode.weather.domain.entityModel.ResponseDataEntity
import com.elslode.weather.domain.useCase.GetWeatherUseCase
import com.elslode.weather.utils.State
import com.elslode.weather.utils.StateResource
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val preferences: SharedPreferences
) : ViewModel() {

    private val _weatherSharedFlow =
        MutableSharedFlow<StateResource<ResponseDataEntity>>()
    val weatherSharedFlow = _weatherSharedFlow

    private val _searchIsNotFoundSharedFlow = MutableSharedFlow<Boolean>()
    val searchIsNotFoundSharedFlow = _searchIsNotFoundSharedFlow.asSharedFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _weatherSharedFlow.emit(StateResource.error(throwable.message.toString(), null))
        }
    }

    private val jobGetListWeather = CoroutineScope(Dispatchers.IO + exceptionHandler)

    suspend fun getWeather(query: String?) =
        query?.let {
            jobGetListWeather.launch{
                getWeatherUseCase.invoke(it).collectLatest { state ->
                    when (state.status) {
                        State.LOADING -> {
                            _weatherSharedFlow.emit(StateResource.loading(null))
                        }
                        State.ERROR -> {
                            _weatherSharedFlow.emit(StateResource.error(state.message.toString(), null))
                        }
                        State.SUCCESS -> {
                            _weatherSharedFlow.emit(StateResource.success(state.data))
                        }
                        State.EMPTY -> {
                            preferences.clearCity()
                            _weatherSharedFlow.emit(StateResource.empty(null))
                            _searchIsNotFoundSharedFlow.emit(true)
                        }
                    }
                }
            }
        }

    fun searchCity(place: String?) {
        jobGetListWeather.launch {
            if (!place.isNullOrBlank()) {
                preferences.put(PrefKeys.CITY_KEY, place)
            } else {
                _searchIsNotFoundSharedFlow.emit(true)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        jobGetListWeather.cancel()
    }
}