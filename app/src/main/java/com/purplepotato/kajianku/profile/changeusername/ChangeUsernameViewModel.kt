package com.purplepotato.kajianku.profile.changeusername

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*

class ChangeUsernameViewModel  : ViewModel() {

    val TAG = "UPDATE-SHARED"

    var auth = FirebaseAuth.getInstance()
    lateinit var user : FirebaseUser

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToProfile = MutableLiveData<Boolean>()
    val  navigateToProfile : LiveData<Boolean>
        get() = _navigateToProfile

    private val _isLoading = MutableLiveData<Boolean>()
    val  isLoading : LiveData<Boolean>
        get() = _isLoading


    fun updateFirebase(name : String){
        uiScope.launch {
            _isLoading.value = true
            withContext(Dispatchers.IO) {

                user = auth.currentUser!!

                // update auth
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()

                user.updateProfile(profileUpdates).addOnCompleteListener {
                    if (it.isSuccessful){
                        Log.i(TAG, "update di auth sukeses")
                        _navigateToProfile.postValue(true)
                    }
                }

                //update firestore
                val data = hashMapOf(
                    "name" to name
                )

                val db = FirebaseFirestore.getInstance()
                db.collection("users")
                    .document(user.uid)
                    .update(data as Map<String, Any>)
                    .addOnSuccessListener {
                        Log.i(TAG, "update di firestore sukses")
                    }

                _isLoading.postValue(false)
            }
        }
    }



}