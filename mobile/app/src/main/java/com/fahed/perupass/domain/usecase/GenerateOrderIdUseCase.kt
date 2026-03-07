package com.fahed.perupass.domain.usecase

import javax.inject.Inject

class GenerateOrderIdUseCase @Inject constructor() {
    operator fun invoke(): String = "ORD-${(1000..9999).random()}"
}
