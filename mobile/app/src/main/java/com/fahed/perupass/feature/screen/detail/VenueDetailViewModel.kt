package com.fahed.perupass.feature.screen.detail

import androidx.lifecycle.SavedStateHandle
import com.fahed.perupass.domain.repository.LocationRepository
import com.fahed.perupass.domain.usecase.CheckProximityUseCase
import com.fahed.perupass.domain.usecase.GetVenueByIdUseCase
import com.fahed.perupass.feature.screen.detail.model.DistanceHelper
import com.fahed.perupass.feature.screen.detail.model.MemberTier
import com.fahed.perupass.feature.shared.core.BaseSideEffectViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class VenueDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getVenueByIdUseCase: GetVenueByIdUseCase,
    private val locationRepository: LocationRepository,
    private val checkProximityUseCase: CheckProximityUseCase
) : BaseSideEffectViewModel<DetailSideEffect>() {

    private val venueId: String = checkNotNull(savedStateHandle["venueId"])

    private val _state = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val state: StateFlow<DetailUiState> = _state.asStateFlow()

    init {
        loadVenueAndTrackLocation()
    }

    fun onIntent(intent: DetailIntent) {
        when (intent) {
            is DetailIntent.ActivateClicked -> onActivateClicked()
            is DetailIntent.CloseClicked -> emitEffect(DetailSideEffect.NavigateBack)
        }
    }

    private fun loadVenueAndTrackLocation() {
        launch {
            getVenueByIdUseCase(venueId)
                .onSuccess { venue ->
                    _state.value = DetailUiState.Success(
                        venue = venue,
                        distanceText = null,
                        isInRange = false,
                        userTier = MemberTier.GOLD // IMPORTANT: hardcoded for MVP — will come from user profile in SPEC-005
                    )
                    locationRepository.getCurrentLocation().collect { userLocation ->
                        val proximity = checkProximityUseCase(
                            userLat = userLocation.latitude,
                            userLng = userLocation.longitude,
                            venueLat = venue.latitude,
                            venueLng = venue.longitude
                        )
                        _state.value = DetailUiState.Success(
                            venue = venue,
                            distanceText = DistanceHelper.format(proximity.distanceMeters),
                            isInRange = proximity.isInRange,
                            userTier = MemberTier.GOLD
                        )
                    }
                }
                .onFailure { error ->
                    _state.value = DetailUiState.Error(error.message ?: "Unknown error")
                }
        }
    }

    private fun onActivateClicked() {
        val current = _state.value as? DetailUiState.Success ?: return
        if (current.isInRange) {
            emitEffect(DetailSideEffect.NavigateToPinValidation(venueId))
        }
    }
}
