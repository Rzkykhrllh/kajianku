package com.purplepotato.kajianku

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.purplepotato.kajianku.auth.login.LoginViewModel
import com.purplepotato.kajianku.auth.sign_up.SignUpViewModel
import com.purplepotato.kajianku.core.KajianRepository
import com.purplepotato.kajianku.di.Injection
import com.purplepotato.kajianku.home.HomeViewModel
import com.purplepotato.kajianku.profile.ProfileViewModel
import com.purplepotato.kajianku.saved_kajian.SavedKajianViewModel

class ViewModelFactory(private val repository: KajianRepository) : ViewModelProvider.Factory {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(): ViewModelFactory = instance
            ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository())
            }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SavedKajianViewModel::class.java) -> {
                SavedKajianViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown View model class : $modelClass")
        }
    }
}