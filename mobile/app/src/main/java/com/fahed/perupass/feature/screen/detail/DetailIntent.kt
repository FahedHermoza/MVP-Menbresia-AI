package com.fahed.perupass.feature.screen.detail

sealed class DetailIntent {
    data object ActivateClicked : DetailIntent()
    data object CloseClicked : DetailIntent()
}
