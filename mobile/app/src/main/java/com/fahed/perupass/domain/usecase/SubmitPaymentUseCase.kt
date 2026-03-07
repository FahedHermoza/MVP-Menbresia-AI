package com.fahed.perupass.domain.usecase

import android.net.Uri
import com.fahed.perupass.domain.model.PaymentRecord
import com.fahed.perupass.domain.repository.PaymentRepository
import javax.inject.Inject

class SubmitPaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository
) {
    suspend operator fun invoke(
        orderId: String,
        imageUri: Uri
    ): Result<Unit> {
        val imageUrl = paymentRepository.uploadProof(orderId, imageUri)
            .getOrElse { return Result.failure(it) }

        val record = PaymentRecord(
            orderId = orderId,
            plan = "gold_select",
            amount = 49.90,
            currency = "PEN",
            imageUrl = imageUrl,
            status = "pending",
            createdAt = System.currentTimeMillis()
        )

        paymentRepository.createPaymentRecord(record)
            .getOrElse { return Result.failure(it) }

        return paymentRepository.activatePass()
    }
}
