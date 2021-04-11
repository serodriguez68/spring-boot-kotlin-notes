package com.sergio.bankSpringTutorial.datasource

import com.sergio.bankSpringTutorial.model.Bank

interface BankDataSource {
    fun retrieveBanks(): Collection<Bank>
}