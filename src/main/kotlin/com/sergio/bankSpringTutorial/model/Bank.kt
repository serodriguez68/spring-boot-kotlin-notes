package com.sergio.bankSpringTutorial.model

import com.fasterxml.jackson.annotation.JsonProperty

/* This tutorial mixes up the responsibility of a Domain Entity with a Data Transfer Object. This
* Class is serving both purposes.
* A data transfer object is an object that goes over the network, by being serialized into a JSON or XML
* representation.
*/
data class Bank(
    /* By default, the JSON serializers / deserializers use the field names for the JSON keys.
    * However, we can customize that with @JsonProperty annotations. */

    @JsonProperty("account_number")
    val accountNumber: String,

    @JsonProperty("trust")
    val trust: Double,

    @JsonProperty("default_transaction_fee")
    val transactionFee: Int
)