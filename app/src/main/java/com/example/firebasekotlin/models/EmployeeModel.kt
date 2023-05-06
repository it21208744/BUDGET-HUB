package com.example.firebasekotlin.models

    data class transModel(
        var transactionId: String? = null,
        var transactionType: String? = null,
        var transactionDescription: String? = null,
        var transactionAmount: String? = null,
        var balance: Int? = null
    )
