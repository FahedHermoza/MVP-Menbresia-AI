package com.fahed.perupass.feature.screen.feed

sealed class Event {
    data object LoadVenues : Event()
    data class VenueClicked(val venueId: String) : Event()
    data class LocationPermissionResult(val granted: Boolean) : Event()
    data class PageChanged(val page: Int) : Event()
}
