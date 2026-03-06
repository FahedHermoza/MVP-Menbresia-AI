package com.fahed.perupass.feature.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fahed.perupass.feature.screen.auth.LoginScreen
import com.fahed.perupass.feature.screen.detail.VenueDetailScreen
import com.fahed.perupass.feature.screen.feed.VibeFeedScreen
import com.fahed.perupass.feature.screen.placeholder.ExploreScreen
import com.fahed.perupass.feature.screen.placeholder.PassesScreen
import com.fahed.perupass.feature.screen.placeholder.ProfileScreen

@Composable
fun AppNavHost(
    startDestination: String = NavRoutes.LOGIN,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavRoutes.LOGIN) {
            LoginScreen(
                onNavigateToFeed = {
                    navController.navigate(NavRoutes.FEED) {
                        popUpTo(NavRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.FEED) {
            VibeFeedScreen(
                onNavigateToDetail = { venueId ->
                    navController.navigate(NavRoutes.venueDetail(venueId))
                },
                onNavigateToExplore = {
                    navController.navigate(NavRoutes.EXPLORE)
                },
                onNavigateToPasses = {
                    navController.navigate(NavRoutes.PASSES)
                },
                onNavigateToProfile = {
                    navController.navigate(NavRoutes.PROFILE)
                }
            )
        }

        composable(
            route = NavRoutes.VENUE_DETAIL,
            arguments = listOf(navArgument("venueId") { type = NavType.StringType })
        ) { backStackEntry ->
            val venueId = backStackEntry.arguments?.getString("venueId") ?: return@composable
            VenueDetailScreen(
                venueId = venueId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPinValidation = { id ->
                    // IMPORTANT: PIN Validation — SPEC-004 (future)
                }
            )
        }

        composable(NavRoutes.EXPLORE) { ExploreScreen() }
        composable(NavRoutes.PASSES) { PassesScreen() }
        composable(NavRoutes.PROFILE) { ProfileScreen() }
    }
}

object NavRoutes {
    const val LOGIN = "login"
    const val FEED = "feed"
    const val EXPLORE = "explore"
    const val PASSES = "passes"
    const val PROFILE = "profile"
    const val VENUE_DETAIL = "venue/{venueId}"

    fun venueDetail(venueId: String) = "venue/$venueId"
}
