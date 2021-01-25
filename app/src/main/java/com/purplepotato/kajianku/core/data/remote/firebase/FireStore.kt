package com.purplepotato.kajianku.core.data.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.core.domain.Kajian
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take

class FireStore {

    companion object {
        @Volatile
        private var instance: FireStore? = null

        fun getInstance(): FireStore = instance ?: synchronized(this) {
            instance ?: FireStore()
        }

        private val database = FirebaseFirestore.getInstance()
        private val currentUser = FirebaseAuth.getInstance().currentUser
    }

    suspend fun queryAllKajian(): Flow<Resource<List<Kajian>>> = flow<Resource<List<Kajian>>> {
        emit(Resource.Loading())
        emit(Resource.Success(emptyList()))
    }.flowOn(Dispatchers.IO).take(2)

    suspend fun queryAllSavedKajian(): Flow<Resource<List<Kajian>>> = flow<Resource<List<Kajian>>> {
        emit(Resource.Loading())
        emit(Resource.Success(emptyList()))
    }.flowOn(Dispatchers.IO).take(2)

}