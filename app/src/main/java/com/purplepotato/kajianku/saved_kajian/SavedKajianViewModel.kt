package com.purplepotato.kajianku.saved_kajian

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.core.data.KajianRepository
import com.purplepotato.kajianku.core.domain.Kajian
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SavedKajianViewModel(private val repository: KajianRepository) : ViewModel() {

    private val _listSavedKajian = MutableLiveData<Resource<List<Kajian>>>()

    val listSavedKajian: LiveData<Resource<List<Kajian>>>
        get() = _listSavedKajian

    init {
        queryAllSavedKajian()
    }

    private fun queryAllSavedKajian() = viewModelScope.launch {
        repository.queryAllSavedKajianFromFireStore().collect {
            _listSavedKajian.postValue(it)
        }
    }
}
