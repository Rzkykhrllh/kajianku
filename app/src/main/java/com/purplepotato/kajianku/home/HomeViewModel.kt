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

    val listSuggestedKajian: LiveData<Resource<List<Kajian>>>
        get() = _listSuggestedKajian

    private val _listPopularKajian = MutableLiveData<Resource<List<Kajian>>>()

    val listPopularKajian: LiveData<Resource<List<Kajian>>>
        get() = _listPopularKajian

    init {
        queryAllSuggestedKajian()
        queryAllPopularKajian()
    }

    private fun queryAllSuggestedKajian() = viewModelScope.launch {
        repository.queryAllSuggestedKajian().collect {
            _listSuggestedKajian.postValue(it)
        }
    }

    private fun queryAllPopularKajian() = viewModelScope.launch {
        repository.queryAllPopularKajian().collect {
            _listPopularKajian.postValue(it)
        }
    }
}