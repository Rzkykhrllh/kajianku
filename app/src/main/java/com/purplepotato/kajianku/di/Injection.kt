package com.purplepotato.kajianku.di

import com.purplepotato.kajianku.core.data.KajianRepository
import com.purplepotato.kajianku.core.data.remote.RemoteDataSource
import com.purplepotato.kajianku.core.data.remote.firebase.FirebaseAuth
import com.purplepotato.kajianku.core.data.remote.firebase.FirebaseDatabase

object Injection {
    fun provideRepository(): KajianRepository {
        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseDatabase = FirebaseDatabase.getInstance()

        val remoteDataSource = RemoteDataSource.getInstance(firebaseAuth, firebaseDatabase)

        return KajianRepository.getInstance(remoteDataSource)
    }
}