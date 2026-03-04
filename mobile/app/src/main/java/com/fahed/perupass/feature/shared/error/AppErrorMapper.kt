package com.fahed.perupass.feature.shared.error

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.fahed.perupass.R

@Composable
fun AppError.toMessage(): String = when (this) {
    AppError.CANCELLED -> stringResource(R.string.error_cancelled)
    AppError.NETWORK   -> stringResource(R.string.error_network)
    AppError.GENERIC   -> stringResource(R.string.error_generic)
}

fun Throwable.toAppError(): AppError = when {
    message?.contains(other = "cancel", ignoreCase = true) == true -> AppError.CANCELLED
    message?.contains(other = "network", ignoreCase = true) == true -> AppError.NETWORK
    else -> AppError.GENERIC
}
