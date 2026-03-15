package com.fahed.perupass.feature.screen.feed.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahed.perupass.R
import com.fahed.perupass.designsystem.MenbresiaColors
import com.fahed.perupass.feature.screen.feed.model.VenueUiModel

private val LocationChipShape = RoundedCornerShape(20.dp)

@Composable
fun VenueOverlay(
    venue: VenueUiModel,
    onLocationChipClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(start = 20.dp, end = 8.dp, bottom = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        if (venue.isOpen) {
            OpenNowBadge()
        }

        Text(
            text = venue.name.uppercase(),
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 32.sp,
            letterSpacing = 0.5.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        LocationChip(
            distanceFormatted = venue.distanceFormatted,
            onClick = onLocationChipClicked
        )

        if (venue.benefitText.isNotBlank()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = stringResource(R.string.feed_benefit_icon_description),
                    tint = MenbresiaColors.Primary,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = stringResource(R.string.feed_benefit_prefix, venue.benefitText),
                    color = MenbresiaColors.Primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.2.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun LocationChip(
    distanceFormatted: String,
    onClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(color = Color(0x33000000), shape = LocationChipShape)
            .border(width = 1.dp, color = MenbresiaColors.Border, shape = LocationChipShape)
            .clickable(
                onClickLabel = stringResource(R.string.feed_location_chip_description),
                role = Role.Button,
                onClick = onClick
            )
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = null,
            tint = MenbresiaColors.TextSecondary,
            modifier = Modifier.size(12.dp)
        )
        Text(
            text = distanceFormatted,
            color = MenbresiaColors.TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.2.sp
        )
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = null,
            tint = MenbresiaColors.TextSecondary,
            modifier = Modifier.size(10.dp)
        )
    }
}

@Composable
private fun OpenNowBadge() {
    Text(
        text = stringResource(R.string.feed_open_now),
        color = Color.White,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp,
        modifier = Modifier
            .background(
                color = MenbresiaColors.Success,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}
