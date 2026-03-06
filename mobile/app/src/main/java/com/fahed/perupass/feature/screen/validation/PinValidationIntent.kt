package com.fahed.perupass.feature.screen.validation

sealed class PinValidationIntent {
    data class DigitPressed(val digit: Int) : PinValidationIntent()
    data object BackspacePressed : PinValidationIntent()
    data object CancelPressed : PinValidationIntent()
}
