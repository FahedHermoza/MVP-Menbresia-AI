package com.fahed.perupass.feature.screen.checkout

sealed class CheckoutSideEffect {
    data object NavigateToConfirmation : CheckoutSideEffect()
    data object OpenImagePicker : CheckoutSideEffect()
}
