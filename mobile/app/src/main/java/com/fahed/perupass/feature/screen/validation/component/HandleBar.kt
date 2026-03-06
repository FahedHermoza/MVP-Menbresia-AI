package com.fahed.perupass.feature.screen.validation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fahed.perupass.designsystem.MenbresiaColors

@Composable
fun HandleBar() {
    Box(
        modifier = Modifier
            .width(40.dp)
            .height(4.dp)
            .background(
                color = MenbresiaColors.TextDisabled,
                shape = RoundedCornerShape(2.dp)
            )
    )
}