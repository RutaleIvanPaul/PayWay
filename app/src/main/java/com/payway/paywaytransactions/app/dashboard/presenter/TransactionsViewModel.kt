package com.payway.paywaytransactions.app.dashboard.presenter

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.RadarData
import com.payway.paywaytransactions.data.dashboard.model.MyResult
import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction
import com.payway.paywaytransactions.domain.dashboard.model.LineDefinition
import com.payway.paywaytransactions.domain.dashboard.usecase.GetLineChartUseCase
import com.payway.paywaytransactions.domain.dashboard.usecase.GetPieChartUseCase
import com.payway.paywaytransactions.domain.dashboard.usecase.GetRadarChartUseCase
import com.payway.paywaytransactions.domain.dashboard.usecase.GetTransactionsUseCase
import com.payway.paywaytransactions.domain.dashboard.util.FilterCriteria
import com.payway.paywaytransactions.domainCore.colorOptions
import com.payway.paywaytransactions.domainCore.decimalFormat
import kotlinx.coroutines.launch
import java.util.Locale.Category
import kotlin.random.Random


class TransactionsViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getLineChartUseCase: GetLineChartUseCase,
    private val getPieChartUseCase: GetPieChartUseCase,
    private val getRadarChartUseCase: GetRadarChartUseCase
) : ViewModel() {
    private val _linedata = MutableLiveData<LineData>()
    val linedata: LiveData<LineData> get() = _linedata

    private val _pieData = MutableLiveData<PieData>()
    val pieData:LiveData<PieData> get() = _pieData

    private val _radarData = MutableLiveData<RadarData>()
    val radarData:LiveData<RadarData> get() = _radarData

    private val _totalTransactions = MutableLiveData<String>()
    val totalTransactions: LiveData<String> get() = _totalTransactions

    private val _totalAmount = MutableLiveData<String>()
    val totalAmount: LiveData<String> get() = _totalAmount

    private val _withdrawAmount = MutableLiveData<String>()
    val withdrawAmount: LiveData<String> get() = _withdrawAmount

    private val _depositAmount = MutableLiveData<String>()
    val depositAmount: LiveData<String> get() = _depositAmount

    //In Memory Hold of Transactions
    //Should be improved with secondary storage
    private var _transactions:List<RemoteTransaction> = listOf()

    private val _distinctCategories = MutableLiveData<List<String>>()
    val distinctCategories:LiveData<List<String>> get() = _distinctCategories

    private val _distinctTypes = MutableLiveData<List<String>>()
    val distinctTypes:LiveData<List<String>> get() = _distinctTypes

    private val _seekBarMinMax = MutableLiveData<Pair<Double,Double>>()
    val seekBarMinMax:MutableLiveData<Pair<Double,Double>> get() = _seekBarMinMax


    fun getTransactions() {
        //If Local is empty
        viewModelScope.launch {
            val result = getTransactionsUseCase.execute()
            when (result) {
                is MyResult.Success -> {
                    _transactions = result.data
                    //populate seek bars
                    _seekBarMinMax.value = getSeekBarMinMaxValues(result.data)
                    //populate categories spinner
                    _distinctCategories.value = getDistinctCategories(result.data)
                    //populate types spinner
                    _distinctTypes.value = getDistinctTypes(result.data)
                    //populate charts
                    getDefaultLineChart(result)
                    getPieChart(result.data)
                    getRadarChart(result.data)
                }

                is MyResult.Error -> {
                    result.exception.message?.let { Log.d("Transactions", it) }
                }
            }
        }

    }

    private fun getLabel(filterCriteria: FilterCriteria): String {
        var label = ""
        filterCriteria.type?.let {
            label += "${it}s"
        }
        filterCriteria.minAmount?.let {
            label += "Min Amount: $it "
        }
        filterCriteria.maxAmount?.let {
            label += "Max Amount: $it "
        }
        filterCriteria.startDate?.let {
            label += "Start Date: $it "
        }
        filterCriteria.endDate?.let {
            label += "End Date: $it "
        }
        filterCriteria.categories?.let {
            if (it.size > 0) {
                label += "Category: "
                it.forEach { category ->
                    label += "$category "
                }
            }
        }

        return label
    }

    fun getFilteredTransactions(filterCriteria: FilterCriteria){
        val filteredTransactions = filterCriteria.getFilteredData(_transactions)
        val label = getLabel(filterCriteria)
        getLineChart(filteredTransactions,label)
        getPieChart(filteredTransactions)
        getRadarChart(filteredTransactions)
    }

    fun getLineChart(transactions: List<RemoteTransaction>, label: String) {
        summarise(transactions)
        _linedata.value = getLineChartUseCase.execute(
            listOf(
                LineDefinition(
                    transactions,
                    colorOptions.shuffled(Random).get(0),
                    label
                )
            )
        )
    }

    fun getPieChart(transactions: List<RemoteTransaction>){
        _pieData.value = getPieChartUseCase.execute(transactions)
    }

    fun getRadarChart(transactions: List<RemoteTransaction>){
        _radarData.value = getRadarChartUseCase.execute(transactions)
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
                        arrayListOf(),
                        null,
                        null
                    ).getFilteredData(result.data),
                    colorOptions.shuffled(Random).get(0),
                    "Deposits"
                ),
                LineDefinition(
                    FilterCriteria(
                        "Withdraw",
                        null,
                        null,
                        arrayListOf(),
                        null,
                        null
                    ).getFilteredData(result.data),
                    colorOptions.shuffled(Random).get(0),
                    "Withdraws"
                )
            )
        )
    }

    fun getDistinctCategories(transactions: List<RemoteTransaction>):List<String> =
        transactions.map { it.Category }.distinct()

    fun getDistinctTypes(transactions: List<RemoteTransaction>):List<String> =
        transactions.map { it.Type }.distinct()

    fun getSeekBarMinMaxValues(transactions: List<RemoteTransaction>):Pair<Double,Double> =
        Pair(transactions.map { it.Amount }.min(),transactions.map { it.Amount }.max())

}
