package com.fahed.perupass.data.source.remote

import com.fahed.perupass.data.model.dto.VenueDTO
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VenueRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getVenues(): List<VenueDTO> =
        firestore.collection("venues")
            .get()
            .await()
            .toObjects(VenueDTO::class.java)

    suspend fun getVenueById(venueId: String): VenueDTO? =
        firestore.collection("venues")
            .document(venueId)
            .get()
            .await()
            .toObject(VenueDTO::class.java)
}
