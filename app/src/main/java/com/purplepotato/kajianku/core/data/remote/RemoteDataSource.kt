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

    suspend fun queryAllSuggestedKajianFromFireStore(): Flow<Resource<List<Kajian>>> =
        fireStore.queryAllSuggestedKajian()

    suspend fun queryAllSavedKajianFromFireStore(): Flow<ApiResponse<List<Kajian>>> =
        fireStore.queryAllSavedKajian()

    suspend fun queryAllPopularKajianFromFireStore(): Flow<Resource<List<Kajian>>> =
        fireStore.queryAllPopularKajian()
}