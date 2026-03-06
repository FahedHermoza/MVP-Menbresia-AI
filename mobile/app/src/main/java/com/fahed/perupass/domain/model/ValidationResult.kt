package com.fahed.perupass.domain.model

sealed class ValidationResult {
    data class Success(val benefit: String, val remainingSeconds: Int = 300) : ValidationResult()
    data class WrongPin(val attemptsRemaining: Int) : ValidationResult()
    data class Blocked(val unblockInSeconds: Int) : ValidationResult()
}
