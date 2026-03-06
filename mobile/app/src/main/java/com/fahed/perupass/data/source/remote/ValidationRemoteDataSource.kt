package com.fahed.perupass.data.source.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ValidationRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    suspend fun getVenuePin(venueId: String): String? =
        firestore.collection("venues")
            .document(venueId)
            .get()
            .await()
            .getString("pin")

    suspend fun logValidation(venueId: String, venueName: String, benefit: String) {
        val userId = auth.currentUser?.uid ?: return
        val log = mapOf(
            "userId" to userId,
            "venueId" to venueId,
            "venueName" to venueName,
            "benefit" to benefit,
            "timestamp" to Date(),
            "status" to "success"
        )
        firestore.collection("validations_log")
            .add(log)
            .await()
    }
}
