package com.fahed.perupass.feature.screen.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.fahed.perupass.feature.shared.error.toMessage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fahed.perupass.R
import com.fahed.perupass.designsystem.MenbresiaColors
import com.fahed.perupass.feature.screen.auth.component.BrandIcon
import com.fahed.perupass.feature.screen.auth.component.GoldRadialGlow
import com.fahed.perupass.feature.screen.auth.component.GoogleSignInButton

@Composable
fun LoginScreen(
    onNavigateToFeed: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val errorMessage = (uiState as? LoginUiState.Error)?.type?.toMessage()

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) onNavigateToFeed()
    }

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            snackbarHostState.showSnackbar(
                message = errorMessage,
                duration = SnackbarDuration.Short
            )
            viewModel.onIntent(LoginIntent.ErrorDismissed)
        }
    }

    Scaffold(
        containerColor = MenbresiaColors.Background,
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    containerColor = MenbresiaColors.Surface,
                    contentColor = MenbresiaColors.TextPrimary,
                    snackbarData = data
                )
            }
        }
    ) { innerPadding ->
        LoginContent(
            uiState = uiState,
            onIntent = viewModel::onIntent,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun LoginContent(
    uiState: LoginUiState,
    onIntent: (LoginIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MenbresiaColors.Background),
        contentAlignment = Alignment.Center
    ) {
        GoldRadialGlow()

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(800)) + slideInVertically(tween(800)) { it / 3 }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(horizontal = 32.dp)
            ) {
                BrandIcon()

                Text(
                    text = stringResource(R.string.login_title),
                    color = MenbresiaColors.TextPrimary,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(R.string.login_subtitle),
                    color = MenbresiaColors.TextSecondary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                GoogleSignInButton(
                    isLoading = uiState is LoginUiState.Loading,
                    onClick = { onIntent(LoginIntent.GoogleSignInClicked) }
                )

                Text(
                    text = stringResource(R.string.login_terms),
                    color = MenbresiaColors.TextDisabled,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
            }
        }
    }
}