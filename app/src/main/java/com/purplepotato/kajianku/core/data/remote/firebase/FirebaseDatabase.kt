package com.purplepotato.kajianku.core.data.remote.firebase

class FirebaseDatabase {

    companion object{
        @Volatile
        private var instance : FirebaseDatabase? = null

        fun getInstance(): FirebaseDatabase = instance ?: synchronized(this){
            instance ?: FirebaseDatabase()
        }
    }
}