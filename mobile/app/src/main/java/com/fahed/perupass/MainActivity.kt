package com.fahed.perupass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fahed.perupass.data.repository.AuthContextHolder
import com.fahed.perupass.designsystem.MenbresiaColors
import com.fahed.perupass.feature.navigation.AppNavHost
import com.fahed.perupass.feature.screen.auth.SplashViewModel
import com.fahed.perupass.ui.theme.PeruPassTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PeruPassTheme {
                val startDestination by splashViewModel.startDestination
                    .collectAsStateWithLifecycle()

                if (startDestination != null) {
                    AppNavHost(startDestination = startDestination!!)
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MenbresiaColors.Background)
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        AuthContextHolder.activityContext = this
    }

    override fun onPause() {
        super.onPause()
        AuthContextHolder.activityContext = null
    }
}