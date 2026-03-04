package com.fahed.perupass.data.source.remote

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRemoteDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val firebaseAuth: FirebaseAuth,
    // Must be the Web OAuth client ID (client_type: 3) from google-services.json
    @WebClientId private val webClientId: String
) {

    private val credentialManager: CredentialManager = CredentialManager.create(context)

    suspend fun signInWithGoogle(activityContext: Context): FirebaseUser {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialResponse = credentialManager.getCredential(
            request = request,
            context = activityContext
        )

        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(
            credentialResponse.credential.data
        )
        val firebaseCredential = GoogleAuthProvider.getCredential(
            googleIdTokenCredential.idToken,
            null
        )

        val authResult = firebaseAuth.signInWithCredential(firebaseCredential).await()
        return authResult.user ?: error("FirebaseUser is null after sign-in")
    }

    fun getCurrentFirebaseUser(): FirebaseUser? = firebaseAuth.currentUser

    suspend fun signOut() {
        firebaseAuth.signOut()
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
    }
}
