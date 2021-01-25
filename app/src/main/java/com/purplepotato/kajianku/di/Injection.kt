package com.purplepotato.kajianku.di

import com.purplepotato.kajianku.core.data.KajianRepository
import com.purplepotato.kajianku.core.data.remote.RemoteDataSource
import com.purplepotato.kajianku.core.data.remote.firebase.FirebaseAuthentication
import com.purplepotato.kajianku.core.data.remote.firebase.FireStore

object Injection {
    fun provideRepository(): KajianRepository {
        val firebaseAuth = FirebaseAuthentication.getInstance()
        val firebaseDatabase = FireStore.getInstance()

        val remoteDataSource = RemoteDataSource.getInstance(firebaseAuth, firebaseDatabase)

        return KajianRepository.getInstance(remoteDataSource)
    }
}