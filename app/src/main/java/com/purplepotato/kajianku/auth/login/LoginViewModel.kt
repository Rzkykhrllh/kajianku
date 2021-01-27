package com.purplepotato.kajianku.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.purplepotato.kajianku.core.data.KajianRepository
import kotlinx.coroutines.*

class LoginViewModel(private val repository: KajianRepository) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    var name: String? = null
    var gender: String? = null
    var birth: String? = null
    var email1: String? = null

    fun login(email: String, password: String) = uiScope.launch {
        _isLoading.value = true
        email1 = email
        withContext(Dispatchers.IO) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    getUserData()
                }.addOnFailureListener {
                    _navigateToHome.value = false
                }
        }
    }

    private fun getUserData() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val db = FirebaseFirestore.getInstance()
                db.collection("users")
                    .document(auth.currentUser!!.uid)
                    .get()
                    .addOnSuccessListener { data ->
                        name = data.getString("name") ?: ""
                        birth = data.getString("birth_date") ?: ""
                        gender = data.getString("gender") ?: ""
                        _isLoading.value = false
                        _navigateToHome.value = true
                    }.addOnFailureListener {
                    }
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