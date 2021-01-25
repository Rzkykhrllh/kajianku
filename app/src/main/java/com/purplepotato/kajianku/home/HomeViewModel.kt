package com.purplepotato.kajianku.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.core.data.KajianRepository
import com.purplepotato.kajianku.core.domain.Kajian
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: KajianRepository) : ViewModel() {

    private val _listSuggestedKajian = MutableLiveData<Resource<List<Kajian>>>()

    val listSuggestedKajian : LiveData<Resource<List<Kajian>>>
        get() = _listSuggestedKajian

    init {
        queryAllSuggestedKajian()
    }

    private fun queryAllSuggestedKajian() = viewModelScope.launch {
        repository.queryAllKajianFromFireStore().collect {
            _listSuggestedKajian.postValue(it)
        }
    }
}