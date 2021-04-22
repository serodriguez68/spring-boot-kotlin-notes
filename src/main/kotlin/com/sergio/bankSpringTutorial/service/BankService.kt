package com.sergio.bankSpringTutorial.service

import com.sergio.bankSpringTutorial.datasource.BankDataSource
import com.sergio.bankSpringTutorial.model.Bank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

/*
* @Service annotation:
* - Tells spring to make this available at runtime
* - Makes it injectable via Spring's DI into other classes
* For any generic bean that should be available in you application
* Context that is not a @Service or an @Repository, you can use @Component.
* */
@Service
/*
* There are 2 possible BankDataSource implementations that could be injected here. How does
* spring know which to inject? There are multiple mechanisms for this:
* 1. Annotating one of the candidates as @Primary (used in this app)
* 2. Giving different qualifiers to the candidates and specifying which qualifier to use in the injection
* point (see commented out example below).
* 3. Annotate the different candidates with different @Profile. Spring will take into account the active application
* profile to see which beans to consider. If you haven't specified which profile is being used, Spring uses a
* "default" profile.  If you don't annotate a particular candidate with @Profile, they will be considered as
* annotated with the default profile.
* */
class BankService(
    @Autowired val bankDataSource: BankDataSource
    // @Qualifier("network") val bankDataSource: BankDataSource
    ) {
    fun getBanks(): Collection<Bank> = bankDataSource.retrieveBanks()
    fun getBank(accountNumber: String): Bank = bankDataSource.retrieveBank(accountNumber)
    fun addBank(bank: Bank): Bank = bankDataSource.createBank(bank)
    fun updateBank(bank: Bank): Bank = bankDataSource.updateBank(bank)
    fun deleteBank(accountNumber: String) = bankDataSource.deleteBank(accountNumber)
}