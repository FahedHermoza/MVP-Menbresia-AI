package com.fahed.perupass.feature.screen.validation

import androidx.lifecycle.SavedStateHandle
import com.fahed.perupass.domain.model.ValidationResult
import com.fahed.perupass.domain.usecase.GetVenueByIdUseCase
import com.fahed.perupass.domain.usecase.ValidatePinUseCase
import com.fahed.perupass.feature.shared.core.BaseSideEffectViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PinValidationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getVenueByIdUseCase: GetVenueByIdUseCase,
    private val validatePinUseCase: ValidatePinUseCase
) : BaseSideEffectViewModel<PinValidationSideEffect>() {

    private val venueId: String = checkNotNull(savedStateHandle["venueId"])

    private val _state = MutableStateFlow<PinValidationUiState>(PinValidationUiState.Idle)
    val state: StateFlow<PinValidationUiState> = _state.asStateFlow()

    private var venueName: String = ""
    private var benefit: String = ""
    private var timerJob: Job? = null

    init {
        loadVenue()
    }

    fun onIntent(intent: PinValidationIntent) {
        when (intent) {
            is PinValidationIntent.DigitPressed -> onDigitPressed(intent.digit)
            is PinValidationIntent.BackspacePressed -> onBackspacePressed()
            is PinValidationIntent.CancelPressed -> emitEffect(PinValidationSideEffect.NavigateBack)
        }
    }

    private fun loadVenue() {
        launch {
            getVenueByIdUseCase(venueId).onSuccess { venue ->
                venueName = venue.name
                // IMPORTANT: hardcoded tier "gold" for MVP — will come from user profile in SPEC-005
                benefit = venue.benefits["gold"] ?: ""
                _state.value = PinValidationUiState.Idle
            }
        }
    }

    private fun onDigitPressed(digit: Int) {
        val current = _state.value
        if (current is PinValidationUiState.Validating || current is PinValidationUiState.Blocked) return

        val currentDigits = when (current) {
            is PinValidationUiState.Entering -> current.digits
            else -> emptyList()
        }
        if (currentDigits.size >= 4) return

        val newDigits = currentDigits + digit
        _state.value = PinValidationUiState.Entering(newDigits)

        if (newDigits.size == 4) {
            validatePin(newDigits.joinToString("") { it.toString() })
        }
    }

    private fun onBackspacePressed() {
        val current = _state.value as? PinValidationUiState.Entering ?: return
        val newDigits = current.digits.dropLast(1)
        _state.value = if (newDigits.isEmpty()) {
            PinValidationUiState.Idle
        } else {
            PinValidationUiState.Entering(newDigits)
        }
    }

    private fun validatePin(pin: String) {
        _state.value = PinValidationUiState.Validating
        launch {
            when (val result = validatePinUseCase(venueId, pin, venueName, benefit)) {
                is ValidationResult.Success -> {
                    _state.value = PinValidationUiState.Success(result.benefit, result.remainingSeconds)
                    startSuccessTimer(result.remainingSeconds)
                }
                is ValidationResult.WrongPin -> {
                    _state.value = PinValidationUiState.Error(result.attemptsRemaining)
                }
                is ValidationResult.Blocked -> {
                    _state.value = PinValidationUiState.Blocked(result.unblockInSeconds)
                    startBlockTimer(result.unblockInSeconds)
                }
            }
        }
    }

    private fun startSuccessTimer(totalSeconds: Int) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var remaining = totalSeconds
            while (remaining > 0) {
                delay(1_000)
                remaining--
                (_state.value as? PinValidationUiState.Success)?.let { current ->
                    _state.value = PinValidationUiState.Success(current.benefit, remaining)
                }
            }
            emitEffect(PinValidationSideEffect.NavigateToFeed)
        }
    }

    private fun startBlockTimer(totalSeconds: Int) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var remaining = totalSeconds
            while (remaining > 0) {
                delay(1_000)
                remaining--
                _state.value = PinValidationUiState.Blocked(remaining)
            }
            _state.value = PinValidationUiState.Idle
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
