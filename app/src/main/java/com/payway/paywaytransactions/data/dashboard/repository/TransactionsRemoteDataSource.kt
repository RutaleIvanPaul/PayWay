package com.payway.paywaytransactions.data.dashboard.repository

import com.payway.paywaytransactions.data.dashboard.TransactionsAPI
import com.payway.paywaytransactions.data.dashboard.model.MyResult
import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction

/**
 * An implementation of the repository concerned with fetching transactions
 * from the API
 */
class TransactionsRemoteDataSource(
    private val transactionsAPI: TransactionsAPI
):TransactionsRepository {
    override suspend fun getTransactions(): MyResult<List<RemoteTransaction>>{
        return try {
            val transactions = transactionsAPI.getTransactions()
            MyResult.Success(transactions)
        } catch (e: Exception) {
            MyResult.Error(e)
        }
    }

}