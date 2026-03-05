package com.fahed.perupass.domain.model.mapper

import com.fahed.perupass.domain.model.Venue
import com.fahed.perupass.feature.screen.feed.model.VenueUiModel

fun Venue.toUiModel(distanceMeters: Double?): VenueUiModel {
    val distanceFormatted = when {
        distanceMeters == null -> "Distancia desconocida"
        distanceMeters < 1000 -> "${distanceMeters.toInt()}m away"
        else -> String.format("%.1fkm away", distanceMeters / 1000)
    }
    val benefitText = benefits["gold"]
        ?: benefits["vibe"]
        ?: benefits["discovery"]
        ?: ""
    return VenueUiModel(
        id = id,
        name = name,
        imageUrl = imageUrl,
        distanceFormatted = distanceFormatted,
        isOpen = isOpen,
        benefitText = benefitText
    )
}