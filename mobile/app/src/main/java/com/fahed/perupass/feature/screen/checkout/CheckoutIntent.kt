package com.fahed.perupass.feature.screen.checkout

import android.net.Uri

sealed class CheckoutIntent {
    data object SelectImage : CheckoutIntent()
    data class ImagePicked(val uri: Uri) : CheckoutIntent()
    data object ConfirmPayment : CheckoutIntent()
    data object Retry : CheckoutIntent()
}
