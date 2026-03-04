package com.fahed.perupass.data.repository

import com.fahed.perupass.data.model.firebase.FirebaseUserMapper.toDomain
import com.fahed.perupass.data.source.remote.AuthRemoteDataSource
import com.fahed.perupass.domain.model.User
import com.fahed.perupass.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun signInWithGoogle(): Result<User> = runCatching {
        val activityContext = AuthContextHolder.activityContext
            ?: error("Activity context not available for CredentialManager")
        remoteDataSource.signInWithGoogle(activityContext).toDomain()
    }

    override fun getCurrentUser(): Flow<User?> = callbackFlow {
        trySend(remoteDataSource.getCurrentFirebaseUser()?.toDomain())
        awaitClose()
    }

    override suspend fun signOut(): Result<Unit> = runCatching {
        remoteDataSource.signOut()
    }

}
