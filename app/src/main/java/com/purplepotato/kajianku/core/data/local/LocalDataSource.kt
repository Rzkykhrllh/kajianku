package com.purplepotato.kajianku.core.data.local

import com.purplepotato.kajianku.core.data.local.entity.SavedKajianEntity
import com.purplepotato.kajianku.core.data.local.room.KajianDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val kajianDao: KajianDao) {

    companion object {
        @Volatile
        private var instance: LocalDataSource? = null

        fun getInstance(kajianDao: KajianDao): LocalDataSource = instance ?: synchronized(this) {
            instance ?: LocalDataSource(kajianDao)
        }
    }

    suspend fun insertSavedKajian(savedKajianEntity: SavedKajianEntity) =
        kajianDao.insertSavedKajian(savedKajianEntity)

    suspend fun insertListSavedKajian(listSavedKajianEntity: List<SavedKajianEntity>) =
        kajianDao.insertListSavedKajian(listSavedKajianEntity)

    suspend fun deleteSavedKajian(savedKajianEntity: SavedKajianEntity) =
        kajianDao.deleteSavedKajian(savedKajianEntity)

    suspend fun deleteAllSavedKajian() = kajianDao.deleteAllSavedKajian()

    fun queryAllSavedKajian(): Flow<List<SavedKajianEntity>> = kajianDao.queryAllSavedKajian()

    suspend fun getSavedKajian(title: String, organizer: String): SavedKajianEntity =
        kajianDao.getSavedKajian(title, organizer)

}