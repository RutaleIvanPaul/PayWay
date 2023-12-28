package com.payway.paywaytransactions.app.dashboard.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.payway.paywaytransactions.domain.dashboard.usecase.GetLineChartUseCase
import com.payway.paywaytransactions.domain.dashboard.usecase.GetPieChartUseCase
import com.payway.paywaytransactions.domain.dashboard.usecase.GetRadarChartUseCase
import com.payway.paywaytransactions.domain.dashboard.usecase.GetTransactionsUseCase

class TransactionsViewModelFactory(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getLineChartUseCase: GetLineChartUseCase,
    private val getPieChartUseCase: GetPieChartUseCase,
    private val getRadarChartUseCase: GetRadarChartUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionsViewModel(getTransactionsUseCase,getLineChartUseCase,getPieChartUseCase,getRadarChartUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
