package com.fahed.perupass.domain.repository

import android.net.Uri
import com.fahed.perupass.domain.model.PaymentRecord

interface PaymentRepository {
    suspend fun uploadProof(orderId: String, imageUri: Uri): Result<String>
    suspend fun createPaymentRecord(record: PaymentRecord): Result<Unit>
    suspend fun activatePass(): Result<Unit>
}
