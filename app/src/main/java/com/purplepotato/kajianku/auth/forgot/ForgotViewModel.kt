package com.purplepotato.kajianku.auth.forgot

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.purplepotato.kajianku.core.data.KajianRepository
import kotlinx.coroutines.*

class ForgotViewModel : ViewModel() {

    lateinit var auth: FirebaseAuth

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var email = ""

    private val _navigateToLogin = MutableLiveData<String>()
    val navigateToLoginFragment : LiveData<String>
        get() = _navigateToLogin

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading  : LiveData<Boolean>
        get() = _isLoading

    init {
        auth = FirebaseAuth.getInstance()
    }

    fun reset(
        email: String,
    ) {
        this.email = email

        uiScope.launch {
            _isLoading.value = true

            withContext(Dispatchers.IO) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(OnCompleteListener { task ->
                        _isLoading.postValue(false)

                        if (task.isSuccessful) {
                            _navigateToLogin.value = "true"

                        } else {
                            _navigateToLogin.value = "fail"
                        }
                    })
            }
        }
    }

}