package com.fahed.perupass.feature.screen.checkout

import android.net.Uri

sealed class CheckoutUiState {
    data class Idle(val orderId: String) : CheckoutUiState()
    data class ImageSelected(val orderId: String, val imageUri: Uri) : CheckoutUiState()
    data class Uploading(val orderId: String) : CheckoutUiState()
    data object Success : CheckoutUiState()
    data class Error(val orderId: String, val message: String) : CheckoutUiState()
}
