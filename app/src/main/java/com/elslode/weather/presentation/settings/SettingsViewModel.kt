package com.elslode.weather.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elslode.weather.domain.entityModel.ResponseDataEntity
import com.elslode.weather.domain.useCase.GetWeatherUseCase
import com.elslode.weather.utils.StateResource
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _searchIsEmptySharedFlow = MutableSharedFlow<Boolean>()
    val searchIsEmptySharedFlow = _searchIsEmptySharedFlow.asSharedFlow()

    private val _searchResultSharedFlow = MutableSharedFlow<StateResource<ResponseDataEntity>>()
    val searchResultSharedFlow = _searchResultSharedFlow.asSharedFlow()


}