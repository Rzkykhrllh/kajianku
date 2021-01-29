package com.purplepotato.kajianku.profile.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.core.data.KajianRepository
import com.purplepotato.kajianku.core.domain.Kajian
import kotlinx.coroutines.flow.collect

class RiwayatKajianViewModel(private val repository: KajianRepository) : ViewModel() {

    val listKajianHistory= liveData<Resource<List<Kajian>>>{
        emit(Resource.Loading())

        try {
            repository.queryAllKajianHistory().collect {
                emit(it)
            }
        }catch (e:Exception){
            emit(Resource.Error(e.message.toString(), emptyList()))
        }
    }

}