package com.elslode.weather.presentation.detailFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elslode.weather.data.repo.RepositoryImpl
import com.elslode.weather.domain.entityModel.ResponseDataEntity
import com.elslode.weather.domain.useCase.GetWeatherForDataUseCase
import com.elslode.weather.utils.State
import com.elslode.weather.utils.StateResource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val getWeatherForDataUseCase: GetWeatherForDataUseCase
) : ViewModel() {

    private val _detailSharedFlow = MutableSharedFlow<StateResource<ResponseDataEntity>>()
    val detailSharedFlow = _detailSharedFlow.asSharedFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _detailSharedFlow.emit(StateResource.error(throwable.message.toString(), null))
        }
    }

    private val jobGetListReview = CoroutineScope(Dispatchers.IO + exceptionHandler)

    fun getWeatherDetail(q: String?, dataTime: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            getWeatherForDataUseCase.invoke(q, dataTime).collectLatest {
                when(it.status) {
                    State.LOADING -> {
                        _detailSharedFlow.emit(StateResource.loading(null))
                    }
                    State.ERROR -> {
                        _detailSharedFlow.emit(StateResource.error(it.message, null))
                    }
                    State.SUCCESS -> {
                        _detailSharedFlow.emit(StateResource.success(it.data))
                    }
                    State.EMPTY -> {
                        _detailSharedFlow.emit(StateResource.empty(null))
                    }
                }
            }
        }
    }

    init {
        jobGetListReview.launch {
            _detailSharedFlow.emit(StateResource.loading(null))
        }
    }

}