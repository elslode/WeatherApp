package com.elslode.weather.domain.useCase

import com.elslode.weather.data.repo.RepositoryImpl
import com.elslode.weather.domain.entityModel.ResponseDataEntity
import com.elslode.weather.utils.StateResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetWeatherForDataUseCase @Inject constructor(
    private val repository: RepositoryImpl
) {
    suspend operator fun invoke(q: String?, dataTime: String?): Flow<StateResource<ResponseDataEntity>> = flow {
        emit(StateResource.loading(null))
        try {
            val weatherDetails = repository.getWeatherForDataDetail(query = q.toString(), dataTime = dataTime.toString())
            if (weatherDetails.data?.current_condition.isNullOrEmpty()) {
                emit(StateResource.empty(null))
            } else {
                emit(StateResource.success(weatherDetails))
            }
        } catch (e: HttpException) {
            emit(StateResource.error(e.localizedMessage ?: "An unexpected error occurred", null))
        } catch (e: IOException) {
            emit(StateResource.error(e.localizedMessage ?: "Internet problem", null))
        }
    }
}