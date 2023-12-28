package com.payway.paywaytransactions.data.dashboard.repository

import com.payway.paywaytransactions.data.dashboard.model.MyResult
import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction
import io.reactivex.Single

interface TransactionsRepository {
    suspend fun getTransactions(): MyResult<List<RemoteTransaction>>
}