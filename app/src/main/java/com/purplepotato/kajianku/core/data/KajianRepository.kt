package com.purplepotato.kajianku.core.data

import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.core.data.remote.RemoteDataSource
import com.purplepotato.kajianku.core.domain.Kajian
import kotlinx.coroutines.flow.Flow

class KajianRepository(
    private val remoteDataSource: RemoteDataSource
) {

    companion object {
        @Volatile
        private var instance: KajianRepository? = null

        fun getInstance(remoteDataSource: RemoteDataSource): KajianRepository =
            instance ?: synchronized(this) {
                instance ?: KajianRepository(remoteDataSource)
            }
    }

    suspend fun queryAllKajianFromFireStore(): Flow<Resource<List<Kajian>>> =
        remoteDataSource.queryAllKajian()

    suspend fun queryAllSavedKajianFromFireStore(): Flow<Resource<List<Kajian>>> =
        remoteDataSource.queryAllSavedKajian()
}