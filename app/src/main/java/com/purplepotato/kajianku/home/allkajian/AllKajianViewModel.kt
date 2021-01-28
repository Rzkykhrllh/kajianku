package com.purplepotato.kajianku.home.allkajian

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.core.data.KajianRepository
import com.purplepotato.kajianku.core.domain.Kajian

class AllKajianViewModel(private val repository: KajianRepository):ViewModel() {

    val listAllKajian : LiveData<Resource<List<Kajian>>>
        get() = repository.queryAllKajian().asLiveData()
}