package com.payway.paywaytransactions.app.dashboard.presenter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction
import com.payway.paywaytransactions.domain.dashboard.usecase.GetTransactionsUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TransactionsViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _transactions = MutableLiveData<List<RemoteTransaction>>()
    val transactions: LiveData<List<RemoteTransaction>> get() = _transactions

    fun getTransactions() {
        val disposable = getTransactionsUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { transactions ->
                    _transactions.value = transactions
                },
                { error ->
                    Log.d("Transactions",error.message!!)
                }
            )

        disposables.add(disposable)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}
