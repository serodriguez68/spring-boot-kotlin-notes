package com.sergio.bankSpringTutorial.datasource.fake

import com.sergio.bankSpringTutorial.datasource.BankDataSource
import com.sergio.bankSpringTutorial.model.Bank
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository

/*
* @Repository marks this class as a bean -> tells spring to load this at runtime
* - Also tells it that is a Repo, which has implications when running in test mode
* */
@Repository
class FakeBankDataSource: BankDataSource {
    val banks = mutableListOf(
        Bank("1234", 2.0, 17),
        Bank("1010", 17.0,  0),
        Bank("9090", 3.10, 100),
    )
    override fun retrieveBanks(): Collection<Bank> = banks

    override fun retrieveBank(accountNumber: String): Bank {
        return banks.firstOrNull { it.accountNumber == accountNumber }
            ?: throw NoSuchElementException("Could not find a bank with account number $accountNumber")
    }

    override fun createBank(bank: Bank): Bank {
        /* Note that using exceptions for data validation is questionable software design.
        But that is what the tutorial is doing...*/
        if (banks.any { it.accountNumber == bank.accountNumber }) {
            throw IllegalArgumentException("Bank with account number ${bank.accountNumber} already exists")
        }
        banks.add(bank)
        return bank
    }

    override fun updateBank(bank: Bank): Bank {
        val index = banks.indexOfFirst { it.accountNumber == bank.accountNumber }
        if (index == -1) throw NoSuchElementException("Bank with account number ${bank.accountNumber} does not exists")

        banks[index] = bank
        return bank
    }

    override fun deleteBank(accountNumber: String) {
        val bank =  banks.firstOrNull { it.accountNumber == accountNumber }
            ?: throw NoSuchElementException("Could not find a bank with account number $accountNumber")

        banks.remove(bank)
    }
}
