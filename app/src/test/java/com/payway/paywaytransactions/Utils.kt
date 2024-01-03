package com.payway.paywaytransactions

import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction

// Sample test data
val expectedTransactions = listOf(
    RemoteTransaction(
        Amount = 1000.0,
        Category = "Internet",
        Service = "Airtel Internet",
        TxFinish = "2023-10-29 14:07:02",
        Type = "Deposit"
    ),
    RemoteTransaction(
        Amount = 23000.0,
        Category = "Mobile Money",
        Service = "Airtel Money",
        TxFinish = "2023-10-29 14:09:40",
        Type = "Deposit"
    )
)