package com.sergio.bankSpringTutorial.datasource.network

import com.sergio.bankSpringTutorial.datasource.BankDataSource
import com.sergio.bankSpringTutorial.datasource.network.dataTransferObject.BankList
import com.sergio.bankSpringTutorial.model.Bank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import java.io.IOException

/*
* WARNING:
* This whole file was meant to be an actual implementation of data source over the network.
* However, the API used in the tutorial was no longer available when we built this sample
* (it times out).
* Only one method has been actually implemented for the sake of showing how to deal with
* network requests and serialization. The rest of the methods have been left as TODOs.
* */


@Repository
/* @Primary tells spring which bean to use if multiple possible beans can be injected */
@Primary
class NetworkBankDataSource(
    /* Rest Template is an HTTP client provided by Spring.
    * Here we are using dependency injection to get Spring to provide it.
    * @see BankSpringTutorialApplication.kt to see how we configured this so that it could be available
    * via dependency Injection */
    @Autowired private val restTemplate: RestTemplate
): BankDataSource {

    override fun retrieveBanks(): Collection<Bank> {
        /*
        Make a network request and parse the response using the Data Transfer Object (DTO) given as a generic
        argument. The DTO is used as template to determine which fields are of interest on and what types they should
         be.

        If the response has additional fields not described in the DTO (BankList in this case), the deserializer
        (Jackson by default) will just ignore them.

        Note that the deserializer is smart enough to figure out how to deal with nested DTOs. In this case, the
        BankList holds multiple banks, so the deserialization maps the JSON array in the response to BankList and
        each element to a Bank.
        * */
        // WARNING: by the time we did this tutorial, this IP was no longer working.
        // val response: ResponseEntity<BankList> = restTemplate.getForEntity<BankList>("http://54.193.31.159/banks")
        //
        // return response.body?.results ?: throw IOException("Could not fetch Banks")
        TODO("Using the incomplete Network Bank Data Source")
    }

    override fun retrieveBank(accountNumber: String): Bank {
        TODO("Using the incomplete Network Bank Data Source")
    }

    override fun createBank(bank: Bank): Bank {
        TODO("Using the incomplete Network Bank Data Source")
    }

    override fun updateBank(bank: Bank): Bank {
        TODO("Using the incomplete Network Bank Data Source")
    }

    override fun deleteBank(accountNumber: String) {
        TODO("Using the incomplete Network Bank Data Source")
    }
}