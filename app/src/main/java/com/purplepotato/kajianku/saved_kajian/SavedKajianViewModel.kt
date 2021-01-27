package com.purplepotato.kajianku.saved_kajian

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.core.data.KajianRepository
import com.purplepotato.kajianku.core.domain.Kajian

class SavedKajianViewModel(private val repository: KajianRepository) : ViewModel() {
    val listSavedKajian: LiveData<Resource<List<Kajian>>>
        get() = repository.queryAllSavedKajian().asLiveData()
}
