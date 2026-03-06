package com.fahed.perupass.feature.screen.detail

import com.fahed.perupass.domain.model.Venue
import com.fahed.perupass.feature.screen.detail.model.MemberTier

sealed class DetailUiState {
    data object Loading : DetailUiState()
    data class Success(
        val venue: Venue,
        val distanceText: String?,
        val isInRange: Boolean,
        val userTier: MemberTier
    ) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}
