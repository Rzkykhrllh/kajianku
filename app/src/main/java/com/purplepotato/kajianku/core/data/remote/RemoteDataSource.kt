package com.purplepotato.kajianku.core.data.remote

import com.purplepotato.kajianku.core.data.remote.firebase.FirebaseAuth
import com.purplepotato.kajianku.core.data.remote.firebase.FirebaseDatabase

class RemoteDataSource(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
) {

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(
            firebaseAuth: FirebaseAuth,
            firebaseDatabase: FirebaseDatabase
        ): RemoteDataSource = instance ?: synchronized(this) {
            instance ?: RemoteDataSource(firebaseAuth, firebaseDatabase)
        }
    }

}