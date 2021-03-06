package com.sergio.bankSpringTutorial.datasource.fake

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class FakeBankDataSourceTest {
    /*
    Here by design we are NOT using any of Spring's Dependency Injection for
    the tests. We are building this ourselves to keep this object as a POJO
    */
    private val fakeDataSource = FakeBankDataSource()

    @Test
    fun `provides a collection of banks`() {
        val banks = fakeDataSource.retrieveBanks()
        assertThat(banks.size).isGreaterThanOrEqualTo(3)
    }
    
    @Test
    fun `provides some mock data`() {
        val banks = fakeDataSource.retrieveBanks()
        assertThat(banks).allMatch{ it.accountNumber.isNotBlank() }
        assertThat(banks).anyMatch{ it.trust != 0.0 }
        assertThat(banks).anyMatch{ it.transactionFee != 0 }
    }
}