package com.purplepotato.kajianku.core.data

import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.core.data.local.LocalDataSource
import com.purplepotato.kajianku.core.data.local.entity.SavedKajianEntity
import com.purplepotato.kajianku.core.data.remote.RemoteDataSource
import com.purplepotato.kajianku.core.domain.Kajian
import com.purplepotato.kajianku.core.util.DataMapper
import kotlinx.coroutines.flow.Flow

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

    fun queryAllSavedKajianFromRemote(): Flow<Resource<List<Kajian>>> =
        remoteDataSource.queryAllSavedKajianFromFireStore()

    fun queryAllSavedKajianFromLocal(): Flow<List<SavedKajianEntity>> =
        localDataSource.queryAllSavedKajian()

    suspend fun insertListSavedKajianToLocal(listSavedKajianEntity: List<SavedKajianEntity>) {
        localDataSource.insertListSavedKajian(listSavedKajianEntity)
    }

    fun queryAllPopularKajian(): Flow<Resource<List<Kajian>>> =
        remoteDataSource.queryAllPopularKajianFromFireStore()

    suspend fun deleteAllSavedKajian() = localDataSource.deleteAllSavedKajian()

    suspend fun deleteSavedKajian(kajian: Kajian) {
        localDataSource.deleteSavedKajian(DataMapper.mapDomainToEntity(kajian))
        remoteDataSource.deleteSavedKajian(kajian.id)
    }

    suspend fun insertSavedKajian(kajian: Kajian) {
        localDataSource.insertSavedKajian(DataMapper.mapDomainToEntity(kajian))
        remoteDataSource.insertSavedKajian(kajian.id)
    }

    suspend fun insertSavedKajianAfterLogin(kajian: Kajian){
        localDataSource.insertSavedKajian(DataMapper.mapDomainToEntity(kajian))
    }

    suspend fun getSavedKajian(title: String, organizer: String): Kajian =
        DataMapper.mapEntityToDomain(localDataSource.getSavedKajian(title, organizer))

    fun deleteSavedKajianAndMoveToUserHistory(id: String) {
        remoteDataSource.deleteSavedKajianAndMoveToUserHistory(id)
    }

    fun queryAllKajianHistory(): Flow<Resource<List<Kajian>>> =
        remoteDataSource.queryAllKajianHistory()

    fun queryAllKajian(): Flow<Resource<List<Kajian>>> = remoteDataSource.queryAllKajian()
}