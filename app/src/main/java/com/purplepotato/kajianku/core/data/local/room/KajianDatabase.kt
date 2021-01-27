package com.purplepotato.kajianku.core.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.purplepotato.kajianku.core.data.local.entity.SavedKajianEntity

@Database(entities = [SavedKajianEntity::class], version = 1, exportSchema = false)
@TypeConverters(RoomTypeConverter::class)
abstract class KajianDatabase : RoomDatabase() {

    abstract fun kajianDao(): KajianDao

    companion object {
        private var instance: KajianDatabase? = null

        fun getInstance(context: Context): KajianDatabase? = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context,
                KajianDatabase::class.java,
                "saved_kajian.db"
            ).fallbackToDestructiveMigration().build()
        }
    }
}