package com.sergio.bankSpringTutorial.service

import com.sergio.bankSpringTutorial.datasource.BankDataSource
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

/* This will be modelled as a unit test*/
internal class BankServiceTest {

    // Again not using DI, creating deps directly and using mocks.
    // This test is completely independent from Spring Boot. It is just a POJO

    // relaxed: whenever a method is called on it, return some dummy value
    // as we don't care about the return value here.
    private val dataSource = mockk<BankDataSource>(relaxed = true)
    private val bankService = BankService(dataSource)

    @Test
    fun `calls its data source to retrieve banks`() {
        val banks = bankService.getBanks()
        // Method from mockk that verifies that a method has been called
        verify(exactly = 1) { dataSource.retrieveBanks() }
    }
}