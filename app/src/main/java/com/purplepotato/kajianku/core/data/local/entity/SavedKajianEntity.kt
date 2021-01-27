package com.purplepotato.kajianku.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_kajian")
data class SavedKajianEntity(
    @PrimaryKey
    @ColumnInfo(name = "reminder_id")
    val reminderId: Long? = -1,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "tag_id")
    val tag: List<String>,

    @ColumnInfo(name = "status")
    val status: String,

    @ColumnInfo(name = "started_at")
    val startedAt: Long,

    @ColumnInfo(name = "speaker")
    val speaker: String,

    @ColumnInfo(name = "register_url")
    val registerUrl: String,

    @ColumnInfo(name = "location")
    val location: String,

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

    @ColumnInfo(name = "total_saved")
    val totalSaved: Long,

    @ColumnInfo(name = "organizer")
    val organizer: String
)