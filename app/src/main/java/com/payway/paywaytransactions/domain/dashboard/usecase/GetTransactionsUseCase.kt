package com.payway.paywaytransactions.domain.dashboard.usecase

import com.payway.paywaytransactions.data.dashboard.model.MyResult
import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction
import com.payway.paywaytransactions.data.dashboard.repository.TransactionsRepository
import io.reactivex.Single

class GetTransactionsUseCase(
    private val transactionsRepository: TransactionsRepository
) {
    suspend fun execute(): MyResult<List<RemoteTransaction>> =
        transactionsRepository.getTransactions()

}