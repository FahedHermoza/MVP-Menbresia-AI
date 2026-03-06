package com.fahed.perupass.feature.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fahed.perupass.domain.model.LatLng
import com.fahed.perupass.domain.repository.LocationRepository
import com.fahed.perupass.domain.usecase.CheckProximityUseCase
import com.fahed.perupass.domain.usecase.GetVenueByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenueDetailViewModel @Inject constructor(
    private val getVenueByIdUseCase: GetVenueByIdUseCase,
    private val locationRepository: LocationRepository,
    private val checkProximityUseCase: CheckProximityUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState(isLoading = true))
    val state: StateFlow<DetailState> = _state.asStateFlow()

    private val _sideEffect = MutableSharedFlow<DetailSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.LoadVenue -> loadVenueData(event.venueId)
            is DetailEvent.OnActivateClicked -> handleActivateClicked(event.venueId)
            is DetailEvent.OnCloseClicked -> handleCloseClicked()
        }
    }

    private fun loadVenueData(venueId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val venueResult = getVenueByIdUseCase(venueId)
            
            venueResult.onSuccess { venue ->
                _state.update { it.copy(isLoading = false, venue = venue) }
                
                // Observe location and recalculate proximity
                locationRepository.getCurrentLocation()
                    .catch { e ->
                        // Handle location error if needed
                    }
                    .collect { userLocation ->
                        val venueLatLng = LatLng(venue.latitude, venue.longitude)
                        val proximityResult = checkProximityUseCase(userLocation, venueLatLng)
                        
                        val distanceText = if (proximityResult.distanceMeters > 999) {
                            "%.1f km".format(proximityResult.distanceMeters / 1000)
                        } else {
                            "${proximityResult.distanceMeters.toInt()}m"
                        }
                        
                        _state.update { 
                            it.copy(
                                distanceText = distanceText,
                                isInRange = proximityResult.isInRange
                            ) 
                        }
                    }
            }.onFailure { error ->
                _state.update { it.copy(isLoading = false, error = error.message) }
            }
        }
    }

    private fun handleActivateClicked(venueId: String) {
        if (_state.value.isInRange) {
            viewModelScope.launch {
                _sideEffect.emit(DetailSideEffect.NavigateToPinValidation(venueId))
            }
        }
    }

    private fun handleCloseClicked() {
        viewModelScope.launch {
            _sideEffect.emit(DetailSideEffect.NavigateBack)
        }
    }
}
