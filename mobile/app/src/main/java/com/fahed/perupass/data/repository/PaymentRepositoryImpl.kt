package com.fahed.perupass.data.repository

import android.net.Uri
import com.fahed.perupass.data.source.remote.PaymentRemoteDataSource
import com.fahed.perupass.domain.model.PaymentRecord
import com.fahed.perupass.domain.repository.PaymentRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepositoryImpl @Inject constructor(
    private val remoteDataSource: PaymentRemoteDataSource
) : PaymentRepository {

    override suspend fun uploadProof(orderId: String, imageUri: Uri): Result<String> =
        runCatching { remoteDataSource.uploadImage(orderId, imageUri) }

    override suspend fun createPaymentRecord(record: PaymentRecord): Result<Unit> =
        runCatching { remoteDataSource.createPaymentRecord(record) }

    override suspend fun activatePass(): Result<Unit> =
        runCatching { remoteDataSource.activateUserPass() }
}
