package com.fahed.perupass.feature.screen.membership

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fahed.perupass.R
import com.fahed.perupass.designsystem.MenbresiaColors
import com.fahed.perupass.feature.screen.membership.component.PlanCard
import com.fahed.perupass.feature.screen.membership.component.PlanData

@Composable
fun MembershipScreen(
    onNavigateToCheckout: () -> Unit
) {
    val plans = listOf(
        PlanData(
            titleRes = R.string.membership_plan_discovery_title,
            priceRes = R.string.membership_plan_discovery_price,
            tagRes = R.string.membership_plan_discovery_tag,
            badgeRes = null,
            benefits = listOf(
                R.string.membership_plan_discovery_benefit_1,
                R.string.membership_plan_discovery_benefit_2,
                R.string.membership_plan_discovery_benefit_3,
                R.string.membership_plan_discovery_benefit_4
            ),
            enabled = false
        ),
        PlanData(
            titleRes = R.string.membership_plan_vibe_title,
            priceRes = R.string.membership_plan_vibe_price,
            tagRes = R.string.membership_plan_vibe_tag,
            badgeRes = R.string.membership_plan_vibe_badge,
            benefits = listOf(
                R.string.membership_plan_vibe_benefit_1,
                R.string.membership_plan_vibe_benefit_2,
                R.string.membership_plan_vibe_benefit_3,
                R.string.membership_plan_vibe_benefit_4
            ),
            enabled = false
        ),
        PlanData(
            titleRes = R.string.membership_plan_gold_title,
            priceRes = R.string.membership_plan_gold_price,
            tagRes = R.string.membership_plan_gold_tag,
            badgeRes = null,
            benefits = listOf(
                R.string.membership_plan_gold_benefit_1,
                R.string.membership_plan_gold_benefit_2,
                R.string.membership_plan_gold_benefit_3,
                R.string.membership_plan_gold_benefit_4
            ),
            enabled = true,
            highlighted = true
        ),
        PlanData(
            titleRes = R.string.membership_plan_black_title,
            priceRes = R.string.membership_plan_black_price,
            tagRes = R.string.membership_plan_black_tag,
            badgeRes = R.string.membership_plan_black_badge,
            benefits = listOf(
                R.string.membership_plan_black_benefit_1,
                R.string.membership_plan_black_benefit_2,
                R.string.membership_plan_black_benefit_3,
                R.string.membership_plan_black_benefit_4
            ),
            enabled = false
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MenbresiaColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 48.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Gold vertical bar
            Column(
                modifier = Modifier
                    .width(4.dp)
                    .height(52.dp)
                    .background(MenbresiaColors.Primary)
            ) {}

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(R.string.membership_header_title),
                    color = MenbresiaColors.TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.membership_header_subtitle),
                    color = MenbresiaColors.TextSecondary,
                    fontSize = 13.sp
                )
            }
        }

        // Plan cards
        plans.forEach { plan ->
            PlanCard(
                plan = plan,
                onSelect = {
                    if (plan.enabled) onNavigateToCheckout()
                }
            )
        }
    }
}
