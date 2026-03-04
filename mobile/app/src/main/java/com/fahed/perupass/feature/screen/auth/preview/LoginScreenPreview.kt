package com.fahed.perupass.feature.screen.auth.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.fahed.perupass.feature.screen.auth.LoginContent

import com.fahed.perupass.feature.screen.auth.LoginUiState

@Preview(showBackground = true, backgroundColor = 0xFF111111)
@Composable
private fun LoginContentPreview() {
    LoginContent(uiState = LoginUiState.Idle, onIntent = {})
}

@Preview(showBackground = true, backgroundColor = 0xFF111111)
@Composable
private fun LoginContentLoadingPreview() {
    LoginContent(uiState = LoginUiState.Loading, onIntent = {})
}