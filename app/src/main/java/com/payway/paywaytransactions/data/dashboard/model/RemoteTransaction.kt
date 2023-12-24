package com.payway.paywaytransactions.data.dashboard.model

data class RemoteTransaction(
    val Amount: Double,
    val Category: String,
    val Service: String,
    val TxFinish: String,
    val Type: String
)