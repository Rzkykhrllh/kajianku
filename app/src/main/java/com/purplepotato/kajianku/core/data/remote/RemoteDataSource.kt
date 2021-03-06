package com.purplepotato.kajianku.core.data.remote

import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.core.data.remote.firebase.FireStore
import com.purplepotato.kajianku.core.data.remote.firebase.FirebaseAuthentication
import com.purplepotato.kajianku.core.domain.Kajian
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    fun queryAllSuggestedKajianFromFireStore(): Flow<Resource<List<Kajian>>> =
        fireStore.queryAllSuggestedKajian()

    fun queryAllSavedKajianFromFireStore(): Flow<Resource<List<Kajian>>> =
        fireStore.queryAllSavedKajian()

    fun queryAllPopularKajianFromFireStore(): Flow<Resource<List<Kajian>>> =
        fireStore.queryAllPopularKajian()

    fun insertSavedKajian(id: String) = fireStore.insertSavedKajian(id)

    fun deleteSavedKajianAndMoveToUserHistory(id: String) =
        fireStore.deleteSavedKajianAndMoveToUserHistory(id)

    fun queryAllKajianHistory(): Flow<Resource<List<Kajian>>> = fireStore.queryAllKajianHistory()

    fun queryAllKajian(): Flow<Resource<List<Kajian>>> = fireStore.queryAllKajian()

    fun deleteSavedKajian(kajianId: String) = fireStore.deleteSavedKajian(kajianId)
}