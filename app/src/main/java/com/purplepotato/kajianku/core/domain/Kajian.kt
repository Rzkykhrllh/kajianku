package com.purplepotato.kajianku.core.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Kajian(
    val title: String,
    val imageUrl: String,
    val description: String,
    val tagId: List<String>,
    val status: String,
    val startedAt: Long,
    val speaker: String,
    val registerUrl: String,
    val location: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable