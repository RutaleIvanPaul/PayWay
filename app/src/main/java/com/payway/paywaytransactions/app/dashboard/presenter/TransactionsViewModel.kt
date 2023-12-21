package com.payway.paywaytransactions.app.dashboard.presenter

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.MPPointF
import com.payway.paywaytransactions.domain.dashboard.usecase.GetTransactionsUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class TransactionsViewModel(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _piedata = MutableLiveData<PieData>()
    val piedata: LiveData<PieData> get() = _piedata

    fun getTransactions() {
        val disposable = getTransactionsUseCase.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { transactions ->
                    val groupedByCategory = transactions.groupBy { it.Category }
                    val pieEntries = groupedByCategory.map { (category, transactions) ->

                        val totalAmount = transactions.sumOf { it.Amount }.toFloat()

                        PieEntry(totalAmount, category)
                    } // Now, pieEntries contains a list of PieEntry objects where each entry represents a distinct category
                // with the total sum of Amount for that category

                    val set = PieDataSet(pieEntries, "Transaction Categories")

                    set.sliceSpace = 3f
                    set.iconsOffset = MPPointF(0F, 40F)
                    set.selectionShift = 5f

                    // add colors
                    val colors: ArrayList<Int> = ArrayList()

                    colors.add(Color.BLACK)
                    colors.add(Color.CYAN)
                    colors.add(Color.YELLOW)
                    colors.add(Color.RED)
                    colors.add(Color.GRAY)
                    colors.add(Color.MAGENTA)

                    colors.add(Color.GREEN)
                    set.colors = colors

                    _piedata.value = PieData(set)
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
