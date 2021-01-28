package com.purplepotato.kajianku.home

import androidx.lifecycle.*
import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.core.data.KajianRepository
import com.purplepotato.kajianku.core.domain.Kajian
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: KajianRepository) : ViewModel() {

    val listSuggestedKajian: LiveData<Resource<List<Kajian>>>
        get() = repository.queryAllSuggestedKajian().asLiveData()

    val listPopularKajian: LiveData<Resource<List<Kajian>>>
        get() = repository.queryAllPopularKajian().asLiveData()

}