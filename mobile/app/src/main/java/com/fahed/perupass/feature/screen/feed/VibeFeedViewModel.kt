package com.fahed.perupass.feature.screen.feed

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fahed.perupass.domain.model.mapper.toUiModel
import com.fahed.perupass.domain.usecase.GetVenuesUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@HiltViewModel
class VibeFeedViewModel @Inject constructor(
    private val getVenuesUseCase: GetVenuesUseCase,
    private val fusedLocationClient: FusedLocationProviderClient
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    private val _navigateToDetail = MutableSharedFlow<String>()
    val navigateToDetail: SharedFlow<String> = _navigateToDetail.asSharedFlow()

    init {
        loadVenues()
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.LoadVenues -> loadVenues()
            is Event.VenueClicked -> viewModelScope.launch {
                _navigateToDetail.emit(event.venueId)
            }
            is Event.LocationPermissionResult -> {
                _state.update { it.copy(locationPermissionGranted = event.granted) }
                if (event.granted) refreshDistances()
            }
            is Event.PageChanged -> _state.update { it.copy(currentPage = event.page) }
        }
    }

    private fun loadVenues() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getVenuesUseCase()
                .onSuccess { venues ->
                    val uiModels = venues.map { it.toUiModel(distanceMeters = null) }
                    _state.update { it.copy(isLoading = false, venues = uiModels) }
                    if (_state.value.locationPermissionGranted) refreshDistances()
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    @SuppressLint("MissingPermission")
    private fun refreshDistances() {
        viewModelScope.launch {
            runCatching {
                val location = fusedLocationClient.lastLocation.await() ?: return@runCatching
                val updatedVenues = _state.value.venues.map { uiModel ->
                    // We need the lat/lng from the original state; enriched via loadVenues
                    uiModel
                }
                _state.update { it.copy(venues = updatedVenues) }
            }
        }
    }

    private fun loadVenuesWithLocation() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val location = runCatching {
                if (_state.value.locationPermissionGranted)
                    fusedLocationClient.lastLocation.await()//TODO
                else null
            }.getOrNull()

            getVenuesUseCase()
                .onSuccess { venues ->
                    val uiModels = venues.map { venue ->
                        val distanceMeters = if (location != null) {
                            haversineDistance(
                                lat1 = location.latitude,
                                lon1 = location.longitude,
                                lat2 = venue.latitude,
                                lon2 = venue.longitude
                            )
                        } else null
                        venue.toUiModel(distanceMeters)
                    }
                    _state.update { it.copy(isLoading = false, venues = uiModels) }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    private fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371000.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        return earthRadius * 2 * atan2(sqrt(a), sqrt(1 - a))
    }
}
