package com.fahed.perupass.feature.screen.validation

sealed class PinValidationUiState {
    data object Idle : PinValidationUiState()
    data class Entering(val digits: List<Int>) : PinValidationUiState()
    data object Validating : PinValidationUiState()
    data class Error(val attemptsLeft: Int) : PinValidationUiState()
    data class Success(val benefit: String, val remainingSeconds: Int) : PinValidationUiState()
    data class Blocked(val remainingSeconds: Int) : PinValidationUiState()
}
