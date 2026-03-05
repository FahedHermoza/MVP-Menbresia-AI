package com.fahed.perupass.feature.screen.feed

import com.fahed.perupass.feature.screen.feed.model.VenueUiModel

data class State(
    val isLoading: Boolean = false,
    val venues: List<VenueUiModel> = emptyList(),
    val currentPage: Int = 0,
    val error: String? = null,
    val locationPermissionGranted: Boolean = false
)
