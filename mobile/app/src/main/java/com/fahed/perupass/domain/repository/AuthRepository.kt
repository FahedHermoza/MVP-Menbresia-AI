package com.fahed.perupass.domain.repository

import com.fahed.perupass.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signInWithGoogle(): Result<User>
    fun getCurrentUser(): Flow<User?>
    suspend fun signOut(): Result<Unit>
}
