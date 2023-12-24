package com.payway.paywaytransactions.data.dashboard.repository

import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction
import io.reactivex.Single

interface TransactionsRepository {
    fun getTransactions(): Single<List<RemoteTransaction>>
}