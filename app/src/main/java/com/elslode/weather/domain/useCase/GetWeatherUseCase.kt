package com.elslode.weather.domain.useCase

import com.elslode.weather.data.repo.RepositoryImpl
import com.elslode.weather.domain.entityModel.ResponseDataEntity
import com.elslode.weather.utils.StateResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(
    private val repository: RepositoryImpl
) {
    operator fun invoke(lat_lon: String): Flow<StateResource<ResponseDataEntity>> = flow {
        emit(StateResource.loading<ResponseDataEntity>(null))
        try {
            val weather = repository.getWeather(lat_lon)
            if (weather.data?.weather.isNullOrEmpty()) {
                emit(StateResource.empty(null))
            } else {
                emit(StateResource.success(weather))
            }


        } catch (e: HttpException) {
            emit(StateResource.error(e.localizedMessage ?: "An unexpected error occurred", null))
        } catch (e: IOException) {
            emit(StateResource.error(e.localizedMessage ?: "Internet problem", null))
        }
    }
}