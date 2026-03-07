package com.fahed.perupass.feature.screen.checkout

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.fahed.perupass.R
import com.fahed.perupass.designsystem.MenbresiaColors

@Composable
fun CheckoutScreen(
    onNavigateToConfirmation: () -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.onIntent(CheckoutIntent.ImagePicked(it)) }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                CheckoutSideEffect.OpenImagePicker -> {
                    imagePicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
                CheckoutSideEffect.NavigateToConfirmation -> onNavigateToConfirmation()
            }
        }
    }

    val errorMessage = (state as? CheckoutUiState.Error)?.message
    val comingSoonMessage = stringResource(R.string.placeholder_coming_soon) //IMPORTANT: Replace comingSoonMessage with errorMessage once Firebase Storage is configured
    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            snackbarHostState.showSnackbar(comingSoonMessage)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MenbresiaColors.Background)
    ) {
        CheckoutContent(
            state = state,
            onIntent = viewModel::onIntent
        )
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun CheckoutContent(
    state: CheckoutUiState,
    onIntent: (CheckoutIntent) -> Unit
) {
    val orderId = when (state) {
        is CheckoutUiState.Idle -> state.orderId
        is CheckoutUiState.ImageSelected -> state.orderId
        is CheckoutUiState.Uploading -> state.orderId
        is CheckoutUiState.Error -> state.orderId
        is CheckoutUiState.Success -> ""
    }
    val selectedImageUri = (state as? CheckoutUiState.ImageSelected)?.imageUri
    val isUploading = state is CheckoutUiState.Uploading
    val canConfirm = state is CheckoutUiState.ImageSelected

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = stringResource(R.string.checkout_title),
            color = MenbresiaColors.TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        // Order code card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MenbresiaColors.Surface, RoundedCornerShape(12.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.checkout_order_label),
                color = MenbresiaColors.TextSecondary,
                fontSize = 11.sp,
                letterSpacing = 1.5.sp
            )
            Text(
                text = orderId,
                color = MenbresiaColors.Primary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        }

        // QR section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MenbresiaColors.Surface, RoundedCornerShape(12.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.checkout_qr_title),
                color = MenbresiaColors.TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(Color.White, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_payment_method),
                    contentDescription = stringResource(R.string.checkout_qr_placeholder),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
            Text(
                text = stringResource(R.string.checkout_phone_number),
                color = MenbresiaColors.TextSecondary,
                fontSize = 13.sp
            )

            val instruction = buildAnnotatedString {
                append(stringResource( R.string.checkout_instruction_prefix))
                withStyle(SpanStyle(color = MenbresiaColors.Primary, fontWeight = FontWeight.Bold)) {
                    append(" $orderId ")
                }
                append(stringResource(R.string.checkout_instruction_suffix))
            }
            Text(
                text = instruction,
                color = MenbresiaColors.TextPrimary,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }

        // Image section
        if (selectedImageUri != null) {
            AsyncImage(
                model = selectedImageUri,
                contentDescription = stringResource(R.string.checkout_proof_description),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Fit
            )
        }

        OutlinedButton(
            onClick = { onIntent(CheckoutIntent.SelectImage) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MenbresiaColors.Primary),
            border = androidx.compose.foundation.BorderStroke(1.dp, MenbresiaColors.Primary),
            enabled = !isUploading
        ) {
            Icon(
                imageVector = Icons.Filled.AddPhotoAlternate,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = stringResource(R.string.checkout_upload_button),
                fontWeight = FontWeight.SemiBold
            )
        }

        if (isUploading) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    color = MenbresiaColors.Primary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = stringResource(R.string.checkout_uploading),
                    color = MenbresiaColors.TextSecondary,
                    fontSize = 13.sp
                )
            }
        }

        Button(
            onClick = { onIntent(CheckoutIntent.ConfirmPayment) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MenbresiaColors.Primary,
                contentColor = Color.Black,
                disabledContainerColor = MenbresiaColors.SurfaceVariant,
                disabledContentColor = MenbresiaColors.TextDisabled
            ),
            enabled = canConfirm
        ) {
            Text(
                text = stringResource(R.string.checkout_confirm_button),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                letterSpacing = 1.sp
            )
        }
    }
}
