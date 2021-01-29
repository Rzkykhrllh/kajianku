package com.purplepotato.kajianku.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.purplepotato.kajianku.core.data.KajianRepository
import com.purplepotato.kajianku.core.domain.Kajian
import com.purplepotato.kajianku.core.util.DataMapper
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take

class ProfileViewModel(private val repository: KajianRepository) : ViewModel() {

    lateinit var auth: FirebaseAuth
    lateinit var user: FirebaseUser

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val listSavedKajianLocal = ArrayList<Kajian>()

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin

    private val _username = MutableLiveData<String?>()
    val username: LiveData<String?>
        get() = _username

    private val _email = MutableLiveData<String?>()
    val email: LiveData<String?>
        get() = _email

    init {
        auth = FirebaseAuth.getInstance()
        var user = auth.currentUser

        /*user?.let {
            _username.value = user.displayName
            _email.value = user.email
        }*/
    }

    fun clearLocalDB() = uiScope.launch {
        repository.deleteAllSavedKajian()
    }

    fun queryAllSavedKajian() = uiScope.launch {
        repository.queryAllSavedKajianFromLocal().take(1).collect { list ->
            list.map {
                listSavedKajianLocal.add(
                    DataMapper.mapEntityToDomain(it)
                )
            }
        }
    }

    fun logout() {
        auth.signOut()
        _navigateToLogin.value = true
    }

    override fun onCleared() {
        super.onCleared()
        uiScope.cancel()
    }
}