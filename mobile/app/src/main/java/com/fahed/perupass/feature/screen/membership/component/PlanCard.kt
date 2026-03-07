package com.fahed.perupass.feature.screen.membership.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahed.perupass.R
import com.fahed.perupass.designsystem.MenbresiaColors

data class PlanData(
    val titleRes: Int,
    val priceRes: Int,
    val tagRes: Int?,
    val badgeRes: Int?,
    val benefits: List<Int>,
    val enabled: Boolean,
    val highlighted: Boolean = false
)

@Composable
fun PlanCard(
    plan: PlanData,
    onSelect: () -> Unit
) {
    val borderColor = when {
        plan.highlighted -> MenbresiaColors.Primary
        plan.enabled -> MenbresiaColors.Border
        else -> MenbresiaColors.Border
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MenbresiaColors.Surface)
            .border(
                width = if (plan.highlighted) 1.5.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .then(
                if (plan.enabled) Modifier.clickable(onClick = onSelect) else Modifier
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header row: tag + price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    plan.tagRes?.let { tagRes ->
                        Text(
                            text = stringResource(tagRes),
                            color = MenbresiaColors.TextSecondary,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = stringResource(plan.titleRes),
                        color = if (plan.enabled) MenbresiaColors.Primary else MenbresiaColors.TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = stringResource(plan.priceRes),
                    color = if (plan.enabled) MenbresiaColors.Primary else MenbresiaColors.TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Optional badge
            plan.badgeRes?.let { badgeRes ->
                Text(
                    text = stringResource(badgeRes),
                    color = if (plan.highlighted) MenbresiaColors.Primary else MenbresiaColors.TextDisabled,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier
                        .background(
                            color = if (plan.highlighted) MenbresiaColors.Primary.copy(alpha = 0.15f)
                            else MenbresiaColors.SurfaceVariant,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            // Benefits list
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                plan.benefits.forEach { benefitRes ->
                    Text(
                        text = stringResource(benefitRes),
                        color = if (plan.enabled) MenbresiaColors.TextSecondary else MenbresiaColors.TextDisabled,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            // Disabled overlay label
            if (!plan.enabled) {
                Text(
                    text = stringResource(R.string.membership_coming_soon),
                    color = MenbresiaColors.TextDisabled,
                    fontSize = 11.sp,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier
                        .align(Alignment.End)
                        .background(MenbresiaColors.SurfaceVariant, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }
    }
}
