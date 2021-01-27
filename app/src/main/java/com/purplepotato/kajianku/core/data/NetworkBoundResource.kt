package com.purplepotato.kajianku.core.data

import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.core.data.remote.ApiResponse
import kotlinx.coroutines.flow.*

abstract class NetworkBoundResource<RequestType, ResultType> {

    private var result: Flow<Resource<ResultType>> = flow {
        emit(Resource.Loading())
        val dbData = loadFromDB().first()
        if (shouldFetch(dbData)) {
            when (val apiResponse = createCall().first()) {
                is ApiResponse.Success -> {
                    saveCallResult(apiResponse.data)
                    emitAll(loadFromDB().map { Resource.Success(it) })
                }

                is ApiResponse.Empty -> {
                    emitAll(loadFromDB().map { Resource.Success(it) })
                }

                is ApiResponse.Error -> {
                    onFetchFailed()
                    emit(Resource.Error<ResultType>(apiResponse.errorMessage))
                }
            }
        }
    }

    open fun onFetchFailed() {}

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract fun loadFromDB(): Flow<ResultType>

    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>

    protected abstract suspend fun saveCallResult(data: RequestType)

    fun asFlow(): Flow<Resource<ResultType>> = result
}