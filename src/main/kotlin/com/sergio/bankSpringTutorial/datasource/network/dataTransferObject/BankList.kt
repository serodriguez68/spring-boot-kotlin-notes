package com.sergio.bankSpringTutorial.datasource.network.dataTransferObject

import com.sergio.bankSpringTutorial.model.Bank

/* A data transfer object is an object that is created for the purpose of describing and holding deserialized data.
* In this case, the DTO (data transfer object) is inside the network package because it is ONLY needed within that
* package and no one outside should know about it */
/* This particular DTO was created to be able to deserialize the structure of data returned by "The New Boston" API */
data class BankList(
    val results: Collection<Bank>
)