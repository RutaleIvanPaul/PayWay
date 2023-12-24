package com.payway.paywaytransactions.app.dashboard.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.payway.paywaytransactions.domain.dashboard.usecase.GetTransactionsUseCase

class TransactionsViewModelFactory(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionsViewModel(getTransactionsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
