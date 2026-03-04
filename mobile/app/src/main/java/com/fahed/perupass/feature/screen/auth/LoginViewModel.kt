package com.fahed.perupass.feature.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fahed.perupass.domain.usecase.SignInWithGoogleUseCase
import com.fahed.perupass.feature.shared.error.toAppError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.GoogleSignInClicked -> signIn()
            is LoginIntent.ErrorDismissed -> _uiState.update { LoginUiState.Idle }
        }
    }

    private fun signIn() {
        viewModelScope.launch {
            _uiState.update { LoginUiState.Loading }
            signInWithGoogleUseCase()
                .onSuccess { _uiState.update { LoginUiState.Success } }
                .onFailure { error -> _uiState.update { LoginUiState.Error(error.toAppError()) } }
        }
    }
}
