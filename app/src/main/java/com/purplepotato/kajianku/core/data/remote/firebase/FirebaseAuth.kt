package com.purplepotato.kajianku.core.data.remote.firebase

class FirebaseAuth {
    companion object{
        @Volatile
        private var instance : FirebaseAuth? = null

        fun getInstance(): FirebaseAuth = instance ?:
        synchronized(this){
            instance ?: FirebaseAuth()
        }
    }
}