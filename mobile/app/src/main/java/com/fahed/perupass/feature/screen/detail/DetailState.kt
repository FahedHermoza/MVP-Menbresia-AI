package com.fahed.perupass.feature.screen.detail

import com.fahed.perupass.domain.model.Venue

data class DetailState(
    val isLoading: Boolean = false,
    val venue: Venue? = null,
    val distanceText: String = "",
    val isInRange: Boolean = false,
    val userTier: String = "Test Tier", // Mock user tier for MVP
    val error: String? = null
)
