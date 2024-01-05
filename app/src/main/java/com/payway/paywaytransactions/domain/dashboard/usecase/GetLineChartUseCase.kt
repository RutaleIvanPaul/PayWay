package com.payway.paywaytransactions.domain.dashboard.usecase

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.payway.paywaytransactions.domain.dashboard.model.LineDefinition
import com.payway.paywaytransactions.domainCore.DateFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GetLineChartUseCase(
    private val lineDataSetFactory: LineDataSetFactory
) {
    fun execute(lineDefinitions: List<LineDefinition>): LineData {
        val lineDataSets = mutableListOf<LineDataSet>()

        lineDefinitions.forEach { lineDefinition ->
            val entriesMap = mutableMapOf<Float, Float>()

            lineDefinition.filteredList.forEach { transaction ->
                val dateKey = DateFormatter.formatDateStringToFloat(transaction.TxFinish)
                val currentAmount = entriesMap[dateKey] ?: 0f
                entriesMap[dateKey] = currentAmount + transaction.Amount.toFloat()
            }

            val entries = entriesMap.entries.map { Entry(it.key, it.value) }

            val lineDataSet = lineDataSetFactory.createLineDataSet(entries,lineDefinition.label)
            lineDataSet.color = lineDefinition.color
            lineDataSet.setDrawValues(false)
            lineDataSets.add(lineDataSet)
        }

        return LineData(lineDataSets as List<ILineDataSet>?)
    }

    class DateAxisFormatter : ValueFormatter() {
        private val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())

        override fun getFormattedValue(value: Float): String {
            return dateFormat.format(Date(value.toLong()))
        }
    }

    interface LineDataSetFactory {
        fun createLineDataSet(yVals:List<Entry>,label:String): LineDataSet
    }

    //Factory to inject to circumvent LineDataSet implementation details for unit tests
    class DefaultLineDataSetFactory : LineDataSetFactory {
        override fun createLineDataSet(yVals: List<Entry>, label: String): LineDataSet {
            return LineDataSet(yVals,label)
        }
    }

}