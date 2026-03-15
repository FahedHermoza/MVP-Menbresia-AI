package com.fahed.perupass.feature.screen.feed.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.fahed.perupass.R
import com.fahed.perupass.feature.screen.feed.model.VenueUiModel

@Composable
fun VenueCard(
    venue: VenueUiModel,
    onVenueClicked: (String) -> Unit,
    onLocationChipClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onVenueClicked(venue.id) }
    ) {
        AsyncImage(
            model = venue.imageUrl,
            contentDescription = stringResource(R.string.feed_venue_image_description, venue.name),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                VenueOverlay(
                    venue = venue,
                    onLocationChipClicked = onLocationChipClicked,
                    modifier = Modifier.weight(1f)
                )

                SideActions(modifier = Modifier.align(Alignment.Bottom))
            }
        }
    }
}
