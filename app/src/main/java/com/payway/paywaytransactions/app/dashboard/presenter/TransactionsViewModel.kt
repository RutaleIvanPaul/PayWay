package com.payway.paywaytransactions.app.dashboard.presenter

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.PieData
import com.payway.paywaytransactions.data.dashboard.model.MyResult
import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction
import com.payway.paywaytransactions.domain.dashboard.model.LineDefinition
import com.payway.paywaytransactions.domain.dashboard.usecase.GetLineChartUseCase
import com.payway.paywaytransactions.domain.dashboard.usecase.GetTransactionsUseCase
import com.payway.paywaytransactions.domain.dashboard.util.FilterCriteria
import kotlinx.coroutines.launch
import java.util.Date


class TransactionsViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getLineChartUseCase: GetLineChartUseCase
) : ViewModel() {
    private val _linedata = MutableLiveData<LineData>()
    val linedata: LiveData<LineData> get() = _linedata

    fun getTransactions() {
        viewModelScope.launch {
            val result = getTransactionsUseCase.execute()
            when (result) {
                is MyResult.Success -> {
                    //populate charts
                    _linedata.value = getLineChartUseCase.execute(
                        listOf(
                            LineDefinition(
                            FilterCriteria( "Deposit",null,null,null,null, null).getFilteredData(result.data),
                            Color.BLUE,
                            "Deposits"
                        ),
                            LineDefinition(
                                FilterCriteria( "Withdraw",null,null,null,null, null).getFilteredData(result.data),
                                Color.RED,
                                "Withdraws"
                            )
                    )
                    )
                }

                is MyResult.Error -> {
                    result.exception.message?.let { Log.d("Transactions", it) }
                }
            }
        }
//        val disposable = getTransactionsUseCase.execute()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(
//                { transactions ->
//                    val groupedByCategory = transactions.groupBy { it.Category }
//                    val pieEntries = groupedByCategory.map { (category, transactions) ->
//
//                        val totalAmount = transactions.sumOf { it.Amount }.toFloat()
//
//                        PieEntry(totalAmount, category)
//                    } // Now, pieEntries contains a list of PieEntry objects where each entry represents a distinct category
//                // with the total sum of Amount for that category
//
//                    val set = PieDataSet(pieEntries, "Transaction Categories")
//
//                    set.sliceSpace = 3f
//                    set.iconsOffset = MPPointF(0F, 40F)
//                    set.selectionShift = 5f
//
//                    // add colors
//                    val colors: ArrayList<Int> = ArrayList()
//
//                    colors.add(Color.BLACK)
//                    colors.add(Color.CYAN)
//                    colors.add(Color.YELLOW)
//                    colors.add(Color.RED)
//                    colors.add(Color.GRAY)
//                    colors.add(Color.MAGENTA)
//
//                    colors.add(Color.GREEN)
//                    set.colors = colors
//
//                    _piedata.value = PieData(set)
//                },
//                { error ->
//                    Log.d("Transactions",error.message!!)
//                }
//            )

    }

}
