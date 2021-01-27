package com.purplepotato.kajianku.auth.sign_up

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.purplepotato.kajianku.core.data.KajianRepository
import kotlinx.coroutines.*

class SignUpViewModel(private val repository: KajianRepository) : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var user: FirebaseUser

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    var name: String = ""
    var birth: String = ""
    var gender: String = ""
    var email: String = ""
    var password: String = ""

    val tag = "inputdesu"

    private val _navigateToHome = MutableLiveData<Boolean>()
    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome


    fun signUp(
        name: String, birth: String, gender: String, email: String, password: String
    ) {
        this.gender = gender
        this.birth = birth
        this.name = name
        Log.i(tag, "di dalam signup")

        uiScope.launch {
            withContext(Dispatchers.IO) {
                _isLoading.value = true
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnFailureListener{
                        Log.i(tag, "di dalam onFailure $it")
                    }
                    .addOnCompleteListener { task ->
                        Log.i(tag, "di dalam onComplete $task")
                        if (task.isSuccessful) {
                            Log.i("inputdesu", "berhasil membuat user")
                            user = auth.currentUser!!
                            updateProfile()
                            addDataToFirestore(user.uid)

                            _isLoading.value = false
                            _navigateToHome.value = true
                        } else {
                            _navigateToHome.value = false
                        }
                    }
            }
        }
    }

    private fun addDataToFirestore(userId : String) {
        val data = hashMapOf(
            "name" to name,
            "birth_date" to birth,
            "gender" to gender
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(userId)
            .set(data)
            .addOnSuccessListener {
                Log.i("signupfire", "berhasil memasukan data ke firestore $userId")
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