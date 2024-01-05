package com.payway.paywaytransactions.domain.dashboard.usecase

import com.payway.paywaytransactions.data.dashboard.model.MyResult
import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction
import com.payway.paywaytransactions.data.dashboard.repository.TransactionsRepository
import io.reactivex.Single

class GetTransactionsUseCase(
    private val transactionsRepository: TransactionsRepository
) {

    //No unit tests to be written for this usecase yet because there is no extra processing or logic
    //the current logic is already covered in unit tests for the repository
    suspend fun execute(): MyResult<List<RemoteTransaction>> =
        transactionsRepository.getTransactions()

}