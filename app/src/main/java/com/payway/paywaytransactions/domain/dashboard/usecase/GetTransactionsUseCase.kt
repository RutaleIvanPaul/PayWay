package com.payway.paywaytransactions.domain.dashboard.usecase

import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction
import io.reactivex.Single

interface GetTransactionsUseCase {
    fun execute(): Single<List<RemoteTransaction>>
}