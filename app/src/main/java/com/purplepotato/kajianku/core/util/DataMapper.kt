package com.purplepotato.kajianku.core.util

import com.purplepotato.kajianku.core.data.local.entity.SavedKajianEntity
import com.purplepotato.kajianku.core.domain.Kajian

object DataMapper {
    fun mapEntityToDomain(savedKajianEntity: SavedKajianEntity): Kajian {
        return Kajian(
            id = savedKajianEntity.id,
            title = savedKajianEntity.title,
            imageUrl = savedKajianEntity.imageUrl,
            description = savedKajianEntity.description,
            tagId = savedKajianEntity.tag,
            status = savedKajianEntity.status,
            startedAt = savedKajianEntity.startedAt,
            speaker = savedKajianEntity.speaker,
            registerUrl = savedKajianEntity.registerUrl,
            location = savedKajianEntity.location,
            latitude = savedKajianEntity.latitude,
            longitude = savedKajianEntity.longitude,
            totalSaved = savedKajianEntity.totalSaved,
            reminderId = savedKajianEntity.reminderId,
            organizer = savedKajianEntity.organizer,
            time = savedKajianEntity.time
        )
    }

    fun mapDomainToEntity(kajian: Kajian): SavedKajianEntity {
        return SavedKajianEntity(
            id = kajian.id,
            title = kajian.title,
            imageUrl = kajian.imageUrl,
            description = kajian.description,
            tag = kajian.tagId,
            status = kajian.status,
            startedAt = kajian.startedAt,
            speaker = kajian.speaker,
            registerUrl = kajian.registerUrl,
            location = kajian.location,
            latitude = kajian.latitude,
            longitude = kajian.longitude,
            totalSaved = kajian.totalSaved,
            reminderId = kajian.reminderId,
            organizer = kajian.organizer,
            time = kajian.time
        )
    }
}