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

/**
 * Use Case concerned with the logic around taking transactions and preparing them for
 * display in the Line chart.
 */
class GetLineChartUseCase(
    private val lineDataSetFactory: LineDataSetFactory
) {
    /**
     * Takes Line definitions,and processes the data to be able
     * to turn the Line definitions into data for the Line Chart.
     * Each line definition represents a line on the line chart.
     */
    fun execute(lineDefinitions: List<LineDefinition>): LineData {
        val lineDataSets = mutableListOf<LineDataSet>()

        lineDefinitions.forEach { lineDefinition ->
            val entriesMap = mutableMapOf<Float, Float>()

            lineDefinition.filteredList.forEach { transaction ->
                val dateKey = DateFormatter.formatDateStringToFloat(transaction.TxFinish)
                //Merge Transactions on the Same date
                val currentAmount = entriesMap[dateKey] ?: 0f
                entriesMap[dateKey] = currentAmount + transaction.Amount.toFloat()
            }

            val entries = entriesMap.entries.map { Entry(it.key, it.value) }//create a list of entries from the map entries

            val lineDataSet = lineDataSetFactory.createLineDataSet(entries,lineDefinition.label)
            lineDataSet.color = lineDefinition.color
            lineDataSet.setDrawValues(false)
            lineDataSets.add(lineDataSet)
        }

        return LineData(lineDataSets as List<ILineDataSet>?)
    }

    /**
     * Used to format the date for display along the Y-axis
     */
    class DateAxisFormatter : ValueFormatter() {
        private val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())

        override fun getFormattedValue(value: Float): String {
            return dateFormat.format(Date(value.toLong()))
        }
    }

    /**
     * Define the Factory interface to decouple the creation of LineDataSet from GetLineChartUseCase
     */
    interface LineDataSetFactory {
        fun createLineDataSet(yVals:List<Entry>,label:String): LineDataSet
    }

    /**Factory to inject to circumvent LineDataSet implementation details for unit tests**/
    class DefaultLineDataSetFactory : LineDataSetFactory {
        override fun createLineDataSet(yVals: List<Entry>, label: String): LineDataSet {
            return LineDataSet(yVals,label)
        }
    }

}