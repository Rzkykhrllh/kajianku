package com.purplepotato.kajianku.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.purplepotato.kajianku.core.data.KajianRepository
import kotlinx.coroutines.*

class LoginViewModel(private val repository: KajianRepository) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    fun login(email: String, password: String) = uiScope.launch {
        withContext(Dispatchers.IO) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    _navigateToHome.value = true
                }.addOnFailureListener {
                    _navigateToHome.value = false
                }
        }
    }

    private fun isAlreadyLoggedIn() {
        val user = auth.currentUser
        _navigateToHome.value = user != null
    }

    init {
        isAlreadyLoggedIn()
    }


    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}