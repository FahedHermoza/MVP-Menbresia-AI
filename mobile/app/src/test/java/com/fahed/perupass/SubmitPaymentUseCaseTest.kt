package com.fahed.perupass

import android.net.Uri
import com.fahed.perupass.domain.model.PaymentRecord
import com.fahed.perupass.domain.repository.PaymentRepository
import com.fahed.perupass.domain.usecase.SubmitPaymentUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SubmitPaymentUseCaseTest {

    private val paymentRepository: PaymentRepository = mockk()
    private val imageUri: Uri = mockk()
    private lateinit var useCase: SubmitPaymentUseCase

    @Before
    fun setUp() {
        useCase = SubmitPaymentUseCase(paymentRepository)
    }

    @Test
    fun `given all steps succeed then returns success`() = runTest {
        coEvery { paymentRepository.uploadProof(any(), any()) } returns Result.success("https://storage/image.jpg")
        coEvery { paymentRepository.createPaymentRecord(any()) } returns Result.success(Unit)
        coEvery { paymentRepository.activatePass() } returns Result.success(Unit)

        val result = useCase("ORD-1234", imageUri)

        assertTrue(result.isSuccess)
    }

    @Test
    fun `given upload fails then returns failure without creating record`() = runTest {
        coEvery { paymentRepository.uploadProof(any(), any()) } returns Result.failure(Exception("Upload failed"))

        val result = useCase("ORD-1234", imageUri)

        assertTrue(result.isFailure)
        coVerify(exactly = 0) { paymentRepository.createPaymentRecord(any()) }
        coVerify(exactly = 0) { paymentRepository.activatePass() }
    }

    @Test
    fun `given create record fails then returns failure without activating pass`() = runTest {
        coEvery { paymentRepository.uploadProof(any(), any()) } returns Result.success("https://storage/image.jpg")
        coEvery { paymentRepository.createPaymentRecord(any()) } returns Result.failure(Exception("Firestore error"))

        val result = useCase("ORD-1234", imageUri)

        assertTrue(result.isFailure)
        coVerify(exactly = 0) { paymentRepository.activatePass() }
    }

    @Test
    fun `given success then creates record with gold select fields`() = runTest {
        val imageUrl = "https://storage/comprobantes/ORD-1234.jpg"
        coEvery { paymentRepository.uploadProof(any(), any()) } returns Result.success(imageUrl)
        coEvery { paymentRepository.createPaymentRecord(any()) } returns Result.success(Unit)
        coEvery { paymentRepository.activatePass() } returns Result.success(Unit)

        useCase("ORD-1234", imageUri)

        coVerify {
            paymentRepository.createPaymentRecord(
                match { record ->
                    record.orderId == "ORD-1234" &&
                        record.plan == "gold_select" &&
                        record.amount == 49.90 &&
                        record.currency == "PEN" &&
                        record.imageUrl == imageUrl &&
                        record.status == "pending"
                }
            )
        }
    }

    @Test
    fun `given success then activates pass`() = runTest {
        coEvery { paymentRepository.uploadProof(any(), any()) } returns Result.success("https://url")
        coEvery { paymentRepository.createPaymentRecord(any()) } returns Result.success(Unit)
        coEvery { paymentRepository.activatePass() } returns Result.success(Unit)

        useCase("ORD-1234", imageUri)

        coVerify(exactly = 1) { paymentRepository.activatePass() }
    }
}
