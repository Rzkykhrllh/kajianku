package com.purplepotato.kajianku.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.purplepotato.kajianku.core.data.KajianRepository
import com.purplepotato.kajianku.core.domain.Kajian
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: KajianRepository) : ViewModel() {

    private var _kajian: Kajian? = null

    fun setKajian(kajian: Kajian) {
        this._kajian = kajian
    }

    fun getKajian() = _kajian

    fun deleteSavedKajian() = viewModelScope.launch {
        repository.deleteSavedKajian(_kajian as Kajian)
    }

    fun insertSavedKajian() = viewModelScope.launch {
        repository.insertSavedKajian(_kajian as Kajian)
    }

}