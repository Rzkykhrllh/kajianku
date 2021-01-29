package com.purplepotato.kajianku.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.core.data.KajianRepository
import com.purplepotato.kajianku.core.domain.Kajian
import kotlinx.coroutines.flow.collect

class HomeViewModel(private val repository: KajianRepository) : ViewModel() {

    val listSuggestedKajian = liveData<Resource<List<Kajian>>> {
        emit(Resource.Loading())
        try {
            repository.queryAllSuggestedKajian().collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString(), emptyList()))
        }
    }


    val listPopularKajian = liveData<Resource<List<Kajian>>> {
        emit(Resource.Loading())

        try {
            repository.queryAllPopularKajian().collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString(), emptyList()))
        }
    }


}