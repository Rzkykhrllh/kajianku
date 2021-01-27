package com.purplepotato.kajianku

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.purplepotato.kajianku.auth.login.LoginViewModel
import com.purplepotato.kajianku.auth.sign_up.SignUpViewModel
import com.purplepotato.kajianku.core.data.KajianRepository
import com.purplepotato.kajianku.detail.DetailViewModel
import com.purplepotato.kajianku.di.Injection
import com.purplepotato.kajianku.home.HomeViewModel
import com.purplepotato.kajianku.home.allkajian.AllKajianViewModel
import com.purplepotato.kajianku.profile.ProfileViewModel
import com.purplepotato.kajianku.saved_kajian.SavedKajianViewModel

class ViewModelFactory(private val repository: KajianRepository) : ViewModelProvider.Factory {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory = instance
            ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
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

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }

            modelClass.isAssignableFrom(AllKajianViewModel::class.java) -> {
                AllKajianViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown View model class : $modelClass")
        }
    }
}