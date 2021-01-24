package com.purplepotato.kajianku.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.purplepotato.kajianku.core.data.KajianRepository
import kotlinx.coroutines.*

class LoginViewModel(private val repository: KajianRepository) : ViewModel() {

    lateinit var auth: FirebaseAuth

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var email = ""
    var password = ""

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome
    
    init {
        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null){
            _navigateToHome.value = true
        }
    }

    fun login(
        email: String,
        password: String
    ) {
        this.email = email
        this.password = password

        uiScope.launch {
            loginFirebase()
        }
    }

    private suspend fun loginFirebase() {
        withContext(Dispatchers.IO) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        _navigateToHome.value = true
                    } else {
                        _navigateToHome.value = false
                    }
                }
        }
    }
}