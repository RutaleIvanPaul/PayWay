package com.payway.paywaytransactions.data.dashboard.repository

import com.payway.paywaytransactions.data.dashboard.TransactionsAPI
import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction
import io.reactivex.Single

class TransactionsRemoteDataSource(
    private val transactionsAPI: TransactionsAPI
):TransactionsRepository {
    override fun getTransactions(): Single<List<RemoteTransaction>> =
        transactionsAPI.getTransactions()

}