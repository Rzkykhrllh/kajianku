package com.purplepotato.kajianku.core.data.remote

import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.core.data.remote.firebase.FireStore
import com.purplepotato.kajianku.core.data.remote.firebase.FirebaseAuthentication
import com.purplepotato.kajianku.core.domain.Kajian
import kotlinx.coroutines.flow.Flow

class RemoteDataSource(
    private val firebaseAuthentication: FirebaseAuthentication,
    private val fireStore: FireStore
) {

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(
            firebaseAuthentication: FirebaseAuthentication,
            fireStore: FireStore
        ): RemoteDataSource = instance ?: synchronized(this) {
            instance ?: RemoteDataSource(firebaseAuthentication, fireStore)
        }
    }

    suspend fun queryAllKajian(): Flow<Resource<List<Kajian>>> = fireStore.queryAllKajian()

    suspend fun queryAllSavedKajian(): Flow<Resource<List<Kajian>>> = fireStore.queryAllSavedKajian()

}