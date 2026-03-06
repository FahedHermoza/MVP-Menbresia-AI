package com.fahed.perupass.feature.screen.detail

sealed class DetailEvent {
    data class LoadVenue(val venueId: String) : DetailEvent()
    data class OnActivateClicked(val venueId: String) : DetailEvent()
    data object OnCloseClicked : DetailEvent()
}
