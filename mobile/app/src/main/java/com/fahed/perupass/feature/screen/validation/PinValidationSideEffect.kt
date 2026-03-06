package com.fahed.perupass.feature.screen.validation

sealed class PinValidationSideEffect {
    data object NavigateBack : PinValidationSideEffect()
    data object NavigateToFeed : PinValidationSideEffect()
}
