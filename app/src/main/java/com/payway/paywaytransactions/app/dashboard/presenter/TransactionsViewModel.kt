package com.payway.paywaytransactions.app.dashboard.presenter

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.PieData
import com.payway.paywaytransactions.data.dashboard.model.MyResult
import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction
import com.payway.paywaytransactions.domain.dashboard.model.LineDefinition
import com.payway.paywaytransactions.domain.dashboard.usecase.GetLineChartUseCase
import com.payway.paywaytransactions.domain.dashboard.usecase.GetPieChartUseCase
import com.payway.paywaytransactions.domain.dashboard.usecase.GetTransactionsUseCase
import com.payway.paywaytransactions.domain.dashboard.util.FilterCriteria
import com.payway.paywaytransactions.domainCore.decimalFormat
import kotlinx.coroutines.launch


class TransactionsViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getLineChartUseCase: GetLineChartUseCase,
    private val getPieChartUseCase: GetPieChartUseCase
) : ViewModel() {
    private val _linedata = MutableLiveData<LineData>()
    val linedata: LiveData<LineData> get() = _linedata

    private val _pieData = MutableLiveData<PieData>()
    val pieData:LiveData<PieData> get() = _pieData

    private val _totalTransactions = MutableLiveData<String>()
    val totalTransactions: LiveData<String> get() = _totalTransactions

    private val _totalAmount = MutableLiveData<String>()
    val totalAmount: LiveData<String> get() = _totalAmount

    private val _withdrawAmount = MutableLiveData<String>()
    val withdrawAmount: LiveData<String> get() = _withdrawAmount

    private val _depositAmount = MutableLiveData<String>()
    val depositAmount: LiveData<String> get() = _depositAmount


    fun getTransactions() {
        viewModelScope.launch {
            val result = getTransactionsUseCase.execute()
            when (result) {
                is MyResult.Success -> {
                    //populate charts
                    getDefaultLineChart(result)
                    getPieChart(result.data)
                }

                is MyResult.Error -> {
                    result.exception.message?.let { Log.d("Transactions", it) }
                }
            }
        }

    }

    fun getLineChart(transactions: List<RemoteTransaction>, color: Int, label: String) {
        summarise(transactions)
        _linedata.value = getLineChartUseCase.execute(
            listOf(
                LineDefinition(
                    transactions,
                    color,
                    label
                )
            )
        )
    }

    fun getPieChart(transactions: List<RemoteTransaction>){
        _pieData.value = getPieChartUseCase.execute(transactions)
    }

    private fun summarise(transactions: List<RemoteTransaction>) {
        _totalTransactions.value = decimalFormat.format(transactions.size)
        _totalAmount.value = decimalFormat.format(transactions.sumOf { it.Amount })
        _depositAmount.value = decimalFormat.format(transactions.filter { it.Type == "Deposit" }.sumOf { it.Amount })
        _withdrawAmount.value = decimalFormat.format(transactions.filter { it.Type == "Withdraw" }.sumOf { it.Amount })

    }

    private fun getDefaultLineChart(result: MyResult.Success<List<RemoteTransaction>>) {
        summarise(result.data)
        _linedata.value = getLineChartUseCase.execute(
            listOf(
                LineDefinition(
                    FilterCriteria(
                        "Deposit",
                        null,
                        null,
                        null,
                        null,
                        null
                    ).getFilteredData(result.data),
                    Color.BLUE,
                    "Deposits"
                ),
                LineDefinition(
                    FilterCriteria(
                        "Withdraw",
                        null,
                        null,
                        null,
                        null,
                        null
                    ).getFilteredData(result.data),
                    Color.RED,
                    "Withdraws"
                )
            )
        )
    }


}
