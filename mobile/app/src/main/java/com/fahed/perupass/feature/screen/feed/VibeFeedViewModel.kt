package com.fahed.perupass.feature.screen.feed

import android.annotation.SuppressLint
import com.fahed.perupass.core.location.HaversineHelper
import com.fahed.perupass.domain.model.mapper.toUiModel
import com.fahed.perupass.domain.usecase.GetVenuesUseCase
import com.fahed.perupass.feature.shared.core.BaseSideEffectViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class VibeFeedViewModel @Inject constructor(
    private val getVenuesUseCase: GetVenuesUseCase,
    private val fusedLocationClient: FusedLocationProviderClient
) : BaseSideEffectViewModel<FeedSideEffect>() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    init {
        loadVenues()
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.LoadVenues -> loadVenues()
            is Event.VenueClicked -> emitEffect(FeedSideEffect.NavigateToDetail(event.venueId))
            is Event.LocationPermissionResult -> {
                _state.update { it.copy(locationPermissionGranted = event.granted) }
                if (event.granted) refreshDistances()
            }
            is Event.PageChanged -> _state.update { it.copy(currentPage = event.page) }
        }
    }

    private fun loadVenues() {
        launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getVenuesUseCase()
                .onSuccess { venues ->
                    val uiModels = venues.map { it.toUiModel(distanceMeters = null) }
                    _state.update { it.copy(isLoading = false, venues = uiModels, domainVenues = venues) }
                    if (_state.value.locationPermissionGranted) refreshDistances()
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    @SuppressLint("MissingPermission")
    private fun refreshDistances() {
        launch {
            runCatching {
                val location = fusedLocationClient.lastLocation.await() ?: return@runCatching
                val updatedVenues = _state.value.domainVenues.map { venue ->
                    val distanceMeters = HaversineHelper.distanceMeters(
                        lat1 = location.latitude,
                        lon1 = location.longitude,
                        lat2 = venue.latitude,
                        lon2 = venue.longitude
                    )
                    venue.toUiModel(distanceMeters)
                }
                _state.update { it.copy(venues = updatedVenues) }
            }
        }
    }

}
