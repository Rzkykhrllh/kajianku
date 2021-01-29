package com.purplepotato.kajianku.saved_kajian

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.purplepotato.kajianku.core.Resource
import com.purplepotato.kajianku.core.data.KajianRepository
import com.purplepotato.kajianku.core.domain.Kajian
import com.purplepotato.kajianku.core.util.DataMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class SavedKajianViewModel(private val repository: KajianRepository) : ViewModel() {
    private val _listSavedKajian = MutableLiveData<Resource<List<Kajian>>>()

    val listSavedKajian: LiveData<Resource<List<Kajian>>>
        get() = _listSavedKajian

    private val viewModelJob = Job()
    private val scope = CoroutineScope(viewModelJob + IO)

    fun queryAllSavedKajianFromRemote() = viewModelScope.launch {
        _listSavedKajian.postValue(Resource.Loading())

        try {
            repository.queryAllSavedKajianFromRemote().collect {
                _listSavedKajian.postValue(it)
            }
        } catch (e: Exception) {
            _listSavedKajian.postValue(Resource.Error(e.message.toString(), emptyList()))
        }
    }

    fun queryAllSavedKajianFromLocal() = viewModelScope.launch {
        _listSavedKajian.postValue(Resource.Loading())

        try {
            repository.queryAllSavedKajianFromLocal().take(1).collect { list ->
                _listSavedKajian.postValue(Resource.Success(list.map {
                    DataMapper.mapEntityToDomain(
                        it
                    )
                }))
            }
        } catch (e: Exception) {
            _listSavedKajian.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun insertListSavedKajianToLocal(kajian: Kajian) = scope.launch {
        repository.insertSavedKajianAfterLogin(kajian)
    }

    fun deleteSavedKajianAndMoveToUserHistory(kajian: Kajian) = scope.launch {
        try {
            repository.deleteSavedKajianAndMoveToUserHistory(kajian.id)
            repository.deleteSavedKajian(kajian)
        } catch (e: Exception) {

        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}
