package com.purplepotato.kajianku.core.data.remote

import com.purplepotato.kajianku.core.data.remote.firebase.FirebaseAuthentication
import com.purplepotato.kajianku.core.data.remote.firebase.FirebaseDatabase

class RemoteDataSource(
    private val firebaseAuthentication: FirebaseAuthentication,
    private val firebaseDatabase: FirebaseDatabase
) {

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(
            firebaseAuthentication: FirebaseAuthentication,
            firebaseDatabase: FirebaseDatabase
        ): RemoteDataSource = instance ?: synchronized(this) {
            instance ?: RemoteDataSource(firebaseAuthentication, firebaseDatabase)
        }
    }

}