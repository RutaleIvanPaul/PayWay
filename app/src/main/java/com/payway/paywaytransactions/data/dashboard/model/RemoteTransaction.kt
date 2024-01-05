package com.payway.paywaytransactions.data.dashboard.model

/**To be the data class to model transactions received from the API response and for purposes
of this demo app, to be used to hold transactions within the rest of the app.**/
data class RemoteTransaction(
    val Amount: Double,
    val Category: String,
    val Service: String,
    val TxFinish: String,
    val Type: String
)