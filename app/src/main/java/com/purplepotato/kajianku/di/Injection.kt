package com.purplepotato.kajianku.di

import android.content.Context
import com.purplepotato.kajianku.core.data.KajianRepository
import com.purplepotato.kajianku.core.data.local.LocalDataSource
import com.purplepotato.kajianku.core.data.local.room.KajianDatabase
import com.purplepotato.kajianku.core.data.remote.RemoteDataSource
import com.purplepotato.kajianku.core.data.remote.firebase.FireStore
import com.purplepotato.kajianku.core.data.remote.firebase.FirebaseAuthentication

object Injection {
    fun provideRepository(context: Context): KajianRepository {
        val firebaseAuth = FirebaseAuthentication.getInstance()
        val firebaseDatabase = FireStore.getInstance()

        val database = KajianDatabase.getInstance(context)

        val remoteDataSource = RemoteDataSource.getInstance(firebaseAuth, firebaseDatabase)
        val localDataSource = LocalDataSource.getInstance(database!!.kajianDao())

        return KajianRepository.getInstance(remoteDataSource, localDataSource)
    }
}