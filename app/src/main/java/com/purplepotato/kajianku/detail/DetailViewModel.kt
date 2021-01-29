package com.purplepotato.kajianku.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.purplepotato.kajianku.core.data.KajianRepository
import com.purplepotato.kajianku.core.domain.Kajian
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: KajianRepository) : ViewModel() {

    private var _kajian: Kajian? = null

    fun setKajian(kajian: Kajian) {
        this._kajian = kajian

        if (kajian.reminderId != -1L) {
            _isKajianAlreadySaved.value = true
        } else {
            getKajianFromDB(kajian.title, kajian.organizer)
        }
    }

    fun getKajian() = _kajian

    private val _isKajianAlreadySaved = MutableLiveData(false)

    val isKajianAlreadySaved: LiveData<Boolean>
        get() = _isKajianAlreadySaved


    fun deleteSavedKajian() = viewModelScope.launch {
        repository.deleteSavedKajian(_kajian as Kajian)
    }

    fun insertSavedKajian() = viewModelScope.launch {
        repository.insertSavedKajian(_kajian as Kajian)
    }

    private fun getKajianFromDB(title: String, organizer: String) = viewModelScope.launch {
        try {
            val savedKajian = repository.getSavedKajian(title, organizer)

            if (savedKajian.reminderId != -1L) {
                _kajian = repository.getSavedKajian(title, organizer)
                _isKajianAlreadySaved.value = true
            }
        } catch (e: Exception) {

        }
    }
}