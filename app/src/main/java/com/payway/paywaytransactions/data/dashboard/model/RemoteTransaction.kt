package com.payway.paywaytransactions.data.dashboard.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class RemoteTransaction(
    val Amount: Double,
    val Category: String,
    val Service: String,
    val TxFinish: String,
    val Type: String
)