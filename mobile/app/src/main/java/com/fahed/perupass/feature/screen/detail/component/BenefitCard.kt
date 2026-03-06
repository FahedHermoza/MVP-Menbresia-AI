package com.fahed.perupass.feature.screen.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BenefitCard(
    tierName: String,
    benefit: String,
    isUserTier: Boolean,
    modifier: Modifier = Modifier
) {
    val tierColor = when (tierName) {
        "Gold Select" -> Color(0xFFD4AF37)
        "Vibe Member" -> Color(0xFFA0A0A0)
        "Black Founder" -> Color(0xFFE0E0E0)
        else -> Color(0xFFA0A0A0)
    }

    val icon = when (tierName) {
        "Gold Select" -> Icons.Default.Star
        "Vibe Member" -> Icons.Default.Bolt
        "Black Founder" -> Icons.Default.Star
        else -> Icons.Default.Bolt
    }

    val baseModifier = modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(4.dp))
        .background(Color(0xFF1C1C1C))
        
    val finalModifier = if (isUserTier) {
        baseModifier.border(1.dp, Color(0xFFD4AF37), RoundedCornerShape(4.dp))
    } else {
        baseModifier
    }

    Row(
        modifier = finalModifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tierColor,
            modifier = Modifier.size(24.dp)
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = tierName,
                color = tierColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = benefit,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}
