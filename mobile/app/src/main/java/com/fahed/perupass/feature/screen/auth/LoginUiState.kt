package com.fahed.perupass.feature.screen.auth

import com.fahed.perupass.feature.shared.error.AppError

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data object Success : LoginUiState()
    data class Error(val type: AppError) : LoginUiState()
}
