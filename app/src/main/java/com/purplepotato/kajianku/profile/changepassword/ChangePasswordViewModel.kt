package com.purplepotato.kajianku.profile.changepassword

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ChangePasswordViewModel : ViewModel() {

    lateinit var auth: FirebaseAuth
    lateinit var user: FirebaseUser


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private val _navigateToProfile = MutableLiveData<Boolean>()
    val navigateToProfile: LiveData<Boolean>
        get() = _navigateToProfile

    lateinit var oldPass : String
    lateinit var newPass : String
    lateinit var email : String

    init {
        auth = FirebaseAuth.getInstance()
    }

    fun changepass(oldPass : String, newPass : String) {

        this.oldPass = oldPass
        this.newPass = newPass

        uiScope.launch {
            changePassFirebase()
        }
    }

    private fun changePassFirebase() {
        user = auth.currentUser!!

        Log.i("changepass", "woyyyy $user")

        val credential = EmailAuthProvider
            .getCredential(user.email!!, oldPass)

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.i("changepass", "auth success")

                    user.updatePassword(newPass).addOnCompleteListener { task->
                        if (task.isSuccessful){
                            Log.i("changepass","Password berhasil di ganti")
                            _navigateToProfile.value = true
                        } else{
                            _navigateToProfile.value = false
                        }
                }
            }


        }


    }

}