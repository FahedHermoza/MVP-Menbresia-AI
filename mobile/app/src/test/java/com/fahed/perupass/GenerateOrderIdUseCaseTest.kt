package com.fahed.perupass

import com.fahed.perupass.domain.usecase.GenerateOrderIdUseCase
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GenerateOrderIdUseCaseTest {

    private lateinit var useCase: GenerateOrderIdUseCase

    @Before
    fun setUp() {
        useCase = GenerateOrderIdUseCase()
    }

    @Test
    fun `generated id starts with ORD- prefix`() {
        val orderId = useCase()
        assertTrue(orderId.startsWith("ORD-"))
    }

    @Test
    fun `generated id has 4 digit numeric suffix`() {
        val orderId = useCase()
        val suffix = orderId.removePrefix("ORD-")
        assertTrue(suffix.all { it.isDigit() })
        assertTrue(suffix.length == 4)
    }

    @Test
    fun `suffix is in valid range 1000 to 9999`() {
        val orderId = useCase()
        val suffix = orderId.removePrefix("ORD-").toInt()
        assertTrue(suffix in 1000..9999)
    }

    @Test
    fun `multiple calls generate different ids`() {
        val ids = (1..20).map { useCase() }.toSet()
        // With a range of 9000 values, 20 calls should very rarely all be the same
        assertTrue(ids.size > 1)
    }
}
