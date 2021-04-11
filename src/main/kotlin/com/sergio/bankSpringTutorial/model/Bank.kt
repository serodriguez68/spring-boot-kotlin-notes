package com.sergio.bankSpringTutorial.model

/* This will be our data transfer object.
* A data transfer object is an object that goes over the network, by being serialized into a JSON  or XML
* representation.
*/
data class Bank(
    val accountNumber: String,
    val trust: Double,
    val transactionFee: Int
)