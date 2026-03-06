package com.fahed.perupass.feature.screen.auth

import com.fahed.perupass.domain.usecase.GetCurrentUserUseCase
import com.fahed.perupass.feature.navigation.NavRoutes
import com.fahed.perupass.feature.shared.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : BaseViewModel() {

    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination.asStateFlow()

    init {
        resolveStartDestination()
    }

    private fun resolveStartDestination() {
        launch {
            val isLoggedIn = getCurrentUserUseCase().first() != null
            _startDestination.value = if (isLoggedIn) NavRoutes.FEED else NavRoutes.LOGIN
        }
    }
}
