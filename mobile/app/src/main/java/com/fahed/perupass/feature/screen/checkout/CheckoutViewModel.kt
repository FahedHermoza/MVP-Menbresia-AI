package com.fahed.perupass.feature.screen.checkout

import android.net.Uri
import com.fahed.perupass.domain.usecase.GenerateOrderIdUseCase
import com.fahed.perupass.domain.usecase.SubmitPaymentUseCase
import com.fahed.perupass.feature.shared.core.BaseSideEffectViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val generateOrderIdUseCase: GenerateOrderIdUseCase,
    private val submitPaymentUseCase: SubmitPaymentUseCase
) : BaseSideEffectViewModel<CheckoutSideEffect>() {

    private val orderId: String = generateOrderIdUseCase()

    private val _state = MutableStateFlow<CheckoutUiState>(CheckoutUiState.Idle(orderId))
    val state: StateFlow<CheckoutUiState> = _state.asStateFlow()

    fun onIntent(intent: CheckoutIntent) {
        when (intent) {
            is CheckoutIntent.SelectImage -> emitEffect(CheckoutSideEffect.OpenImagePicker)
            is CheckoutIntent.ImagePicked -> onImagePicked(intent.uri)
            is CheckoutIntent.ConfirmPayment -> confirmPayment()
            is CheckoutIntent.Retry -> _state.value = CheckoutUiState.Idle(orderId)
        }
    }

    private fun onImagePicked(uri: Uri) {
        _state.value = CheckoutUiState.ImageSelected(orderId, uri)
    }

    private fun confirmPayment() {
        val current = _state.value as? CheckoutUiState.ImageSelected ?: return

        _state.value = CheckoutUiState.Uploading(orderId)
        launch {
            submitPaymentUseCase(
                orderId = orderId,
                imageUri = current.imageUri
            ).fold(
                onSuccess = {
                    _state.value = CheckoutUiState.Success
                    emitEffect(CheckoutSideEffect.NavigateToConfirmation)
                },
                onFailure = { error ->
                    _state.value = CheckoutUiState.Error(
                        orderId = orderId,
                        message = error.message ?: ""
                    )
                }
            )
        }
    }
}
