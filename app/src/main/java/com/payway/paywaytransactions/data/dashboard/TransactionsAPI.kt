package com.payway.paywaytransactions.data.dashboard

import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction
import retrofit2.http.GET

interface TransactionsAPI {
    @GET("transactions")
    suspend fun getTransactions():List<RemoteTransaction>

}