package com.fahed.perupass.feature.screen.detail.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.fahed.perupass.R

enum class MemberTier(
    val key: String,
    @StringRes val labelRes: Int,
    val icon: ImageVector
) {
    VIBE("vibe", R.string.detail_tier_vibe, Icons.Filled.Bolt),
    GOLD("gold", R.string.detail_tier_gold, Icons.Filled.Star),
    BLACK("black", R.string.detail_tier_black, Icons.Filled.EmojiEvents)
}
