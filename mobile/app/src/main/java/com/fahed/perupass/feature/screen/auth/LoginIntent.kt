package com.fahed.perupass.feature.screen.auth

sealed class LoginIntent {
    data object GoogleSignInClicked : LoginIntent()
    data object ErrorDismissed : LoginIntent()
}
