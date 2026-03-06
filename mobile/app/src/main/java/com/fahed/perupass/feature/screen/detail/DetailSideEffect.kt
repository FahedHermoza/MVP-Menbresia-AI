package com.fahed.perupass.feature.screen.detail

sealed class DetailSideEffect {
    data class NavigateToPinValidation(val venueId: String) : DetailSideEffect()
    data object NavigateBack : DetailSideEffect()
}
