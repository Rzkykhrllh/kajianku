package com.purplepotato.kajianku.auth.sign_up

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.purplepotato.kajianku.core.data.KajianRepository
import kotlinx.coroutines.*

class SignUpViewModel(private val repository: KajianRepository) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var user: FirebaseUser

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var name: String = ""
    var birth: String = ""
    var gender: String = ""
    var email: String = ""
    var password: String = ""

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome


    fun signUp(
        name: String, birth: String, gender: String, email: String, password: String
    ) {

        uiScope.launch {
            withContext(Dispatchers.IO) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.i("inputdesu", "berhasil membuat user")
                            user = auth.currentUser!!
                            updateProfile()
                            _navigateToHome.value = true
                        } else {
                            _navigateToHome.value = false
                        }
                    }
            }
        }
    }

    private fun updateProfile() {
        /** profile update request */
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        /** run update request */
        user.updateProfile(profileUpdates).addOnCompleteListener {
            Log.i("inputdesu profile", "berhasil di update")
        }
    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}