package com.purplepotato.kajianku.core.data.remote.firebase

class FirebaseAuthentication {
    companion object{
        @Volatile
        private var instance : FirebaseAuthentication? = null

        fun getInstance(): FirebaseAuthentication = instance ?:
        synchronized(this){
            instance ?: FirebaseAuthentication()
        }
    }
}