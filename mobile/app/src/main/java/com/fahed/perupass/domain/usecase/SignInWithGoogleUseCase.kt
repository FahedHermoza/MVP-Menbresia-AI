package com.fahed.perupass.domain.usecase

import com.fahed.perupass.domain.model.User
import com.fahed.perupass.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<User> = authRepository.signInWithGoogle()
}
