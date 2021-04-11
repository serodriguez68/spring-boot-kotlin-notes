package com.sergio.bankSpringTutorial.datasource.mock

import com.sergio.bankSpringTutorial.datasource.BankDataSource
import com.sergio.bankSpringTutorial.model.Bank
import org.springframework.stereotype.Repository

/*
* @Repository marks this class as a bean -> tells spring to load this at runtime
* - Also tells it that is a Repo, which has implications when running in test mode
* */
@Repository
class MockBankDataSource: BankDataSource {
    val banks = listOf(
        Bank("1234", 2.0, 17),
        Bank("1010", 17.0, 0),
        Bank("9090", 3.10, 100),
    )
    override fun retrieveBanks(): Collection<Bank> = banks
}