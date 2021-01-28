package com.purplepotato.kajianku.core.data

import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.core.data.local.LocalDataSource
import com.purplepotato.kajianku.core.data.remote.ApiResponse
import com.purplepotato.kajianku.core.data.remote.RemoteDataSource
import com.purplepotato.kajianku.core.domain.Kajian
import com.purplepotato.kajianku.core.util.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class KajianRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) {

    companion object {
        @Volatile
        private var instance: KajianRepository? = null

        fun getInstance(
            remoteDataSource: RemoteDataSource,
            localDataSource: LocalDataSource
        ): KajianRepository =
            instance ?: synchronized(this) {
                instance ?: KajianRepository(remoteDataSource, localDataSource)
            }
    }

    fun queryAllSuggestedKajian(): Flow<Resource<List<Kajian>>> =
        remoteDataSource.queryAllSuggestedKajianFromFireStore()

    fun queryAllSavedKajian(): Flow<Resource<List<Kajian>>> =
        object : NetworkBoundResource<List<Kajian>, List<Kajian>>() {
            override fun shouldFetch(data: List<Kajian>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun loadFromDB(): Flow<List<Kajian>> {
                return localDataSource.queryAllSavedKajian().map { list ->
                    list.map { DataMapper.mapEntityToDomain(it) }
                }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<Kajian>>> {
                return remoteDataSource.queryAllSavedKajianFromFireStore()
            }

            override suspend fun saveCallResult(data: List<Kajian>) {
                localDataSource.insertListSavedKajian(data.map {
                    DataMapper.mapDomainToEntity(it)
                })
            }
        }.asFlow()

    fun queryAllPopularKajian(): Flow<Resource<List<Kajian>>> =
        remoteDataSource.queryAllPopularKajianFromFireStore()

    suspend fun deleteAllSavedKajian() = localDataSource.deleteAllSavedKajian()

    suspend fun deleteSavedKajian(kajian: Kajian) =
        localDataSource.deleteSavedKajian(DataMapper.mapDomainToEntity(kajian))

    suspend fun insertSavedKajian(kajian: Kajian) {
        localDataSource.insertSavedKajian(DataMapper.mapDomainToEntity(kajian))
        remoteDataSource.insertSavedKajian(kajian.id)
    }

    suspend fun getSavedKajian(title: String, organizer: String): Kajian =
        DataMapper.mapEntityToDomain(localDataSource.getSavedKajian(title, organizer))

    fun deleteSavedKajianAndMoveToUserHistory(id: String) =
        remoteDataSource.deleteSavedKajianAndMoveToUserHistory(id)

    fun queryAllKajianHistory(): Flow<Resource<List<Kajian>>> = remoteDataSource.queryAllKajianHistory()

    fun queryAllKajian(): Flow<Resource<List<Kajian>>> = remoteDataSource.queryAllKajian()
}