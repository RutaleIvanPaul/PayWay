package com.payway.paywaytransactions.data.dashboard.repository

import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction
import com.payway.paywaytransactions.domain.dashboard.usecase.GetTransactionsUseCase
import io.reactivex.Single

class GetTransactionsUseCaseImpl(
    private val transactionsRepository: TransactionsRepository
):GetTransactionsUseCase {
    override fun execute(): Single<List<RemoteTransaction>> =
        transactionsRepository.getTransactions()

}