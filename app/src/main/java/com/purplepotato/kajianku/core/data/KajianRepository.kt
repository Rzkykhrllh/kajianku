package com.purplepotato.kajianku.core.data

import com.purplepotato.kajianku.core.data.remote.RemoteDataSource

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
}