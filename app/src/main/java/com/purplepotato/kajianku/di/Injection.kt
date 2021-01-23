package com.purplepotato.kajianku.di

import com.purplepotato.kajianku.core.KajianRepository
import com.purplepotato.kajianku.core.remote.RemoteDataSource
import com.purplepotato.kajianku.core.remote.firebase.FirebaseAuth
import com.purplepotato.kajianku.core.remote.firebase.FirebaseDatabase

object Injection {
    fun provideRepository(): KajianRepository {
        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseDatabase = FirebaseDatabase.getInstance()

        val remoteDataSource = RemoteDataSource.getInstance(firebaseAuth, firebaseDatabase)

        return KajianRepository.getInstance(remoteDataSource)
    }
}