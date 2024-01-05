package com.payway.paywaytransactions.data.dashboard.repository

import com.payway.paywaytransactions.data.dashboard.model.MyResult
import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction

/**
 * Universal definition data access from the repository
 */
interface TransactionsRepository {
    suspend fun getTransactions(): MyResult<List<RemoteTransaction>>
}