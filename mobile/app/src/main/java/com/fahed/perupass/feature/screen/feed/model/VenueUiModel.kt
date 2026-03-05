package com.fahed.perupass.feature.screen.feed.model

data class VenueUiModel(
    val id: String,
    val name: String,
    val imageUrl: String,
    val distanceFormatted: String,
    val isOpen: Boolean,
    val benefitText: String
)