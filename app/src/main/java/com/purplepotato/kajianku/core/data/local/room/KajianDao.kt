package com.purplepotato.kajianku.core.data.local.room

import androidx.room.*
import com.purplepotato.kajianku.core.data.local.entity.SavedKajianEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KajianDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedKajian(savedKajianEntity: SavedKajianEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListSavedKajian(listSavedKajianEntity: List<SavedKajianEntity>)

    @Delete
    suspend fun deleteSavedKajian(savedKajianEntity: SavedKajianEntity)

    @Query("DELETE FROM saved_kajian")
    suspend fun deleteAllSavedKajian()

    @Query("SELECT * FROM saved_kajian ORDER BY started_at ASC")
    fun queryAllSavedKajian(): Flow<List<SavedKajianEntity>>

}