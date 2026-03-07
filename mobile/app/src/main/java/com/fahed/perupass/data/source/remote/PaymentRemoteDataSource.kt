package com.fahed.perupass.data.source.remote

import android.net.Uri
import com.fahed.perupass.domain.model.PaymentRecord
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRemoteDataSource @Inject constructor(
    private val storage: FirebaseStorage,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    suspend fun uploadImage(orderId: String, uri: Uri): String {
        val ref = storage.reference.child("receipts/$orderId.jpg")
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }

    suspend fun createPaymentRecord(record: PaymentRecord) {
        val user = auth.currentUser ?: return
        val data = mapOf(
            "orderId" to record.orderId,
            "userId" to user.uid,
            "userEmail" to (user.email ?: ""),
            "plan" to record.plan,
            "amount" to record.amount,
            "currency" to record.currency,
            "imageUrl" to record.imageUrl,
            "status" to record.status,
            "createdAt" to Date(record.createdAt)
        )
        firestore.collection("outstanding_payments")
            .document(record.orderId)
            .set(data)
            .await()
    }

    suspend fun activateUserPass() {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users")
            .document(userId)
            .update("hasPase", true)
            .await()
    }
}
