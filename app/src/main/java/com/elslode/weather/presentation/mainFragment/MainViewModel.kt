package com.elslode.weather.presentation.mainFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elslode.weather.data.repo.RepositoryImpl
import com.elslode.weather.domain.entityModel.ResponseData
import com.elslode.weather.utils.StateResource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repositoryImpl: RepositoryImpl
) : ViewModel() {

    private val _weatherSharedFlow = MutableSharedFlow<StateResource<ResponseData>>()
    val weatherSharedFlow = _weatherSharedFlow.asSharedFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _weatherSharedFlow.emit(StateResource.error(throwable.message.toString(), null))
        }
    }

    private val jobGetListReview = CoroutineScope(Dispatchers.IO + exceptionHandler)

    suspend fun getWeather(lat_lon: String) =
        jobGetListReview.launch {
            _weatherSharedFlow.emit(StateResource.success(repositoryImpl.getWeather(lat_lon)))
        }

    init {
        jobGetListReview.launch {
            _weatherSharedFlow.emit(StateResource.loading(null))
        }
    }

}