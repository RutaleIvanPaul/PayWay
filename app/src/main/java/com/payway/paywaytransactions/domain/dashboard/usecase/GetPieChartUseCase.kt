package com.payway.paywaytransactions.domain.dashboard.usecase

import android.graphics.Color
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction
import com.payway.paywaytransactions.domainCore.ColorProvider
import com.payway.paywaytransactions.domainCore.decimalFormat
import kotlin.random.Random

class GetPieChartUseCase(
    private val pieDataSetFactory: GetPieChartUseCase.PieDataSetFactory
) {

    fun execute(transactions: List<RemoteTransaction>): PieData {
        // Filtering transactions by category
        val categories = transactions.map { it.Category }.distinct()
        // Get total amount
        val totalAmount = transactions.sumOf { it.Amount }

        // Calculate percentages for each category and add to Pie Entries
        val pieEntries = mutableListOf<PieEntry>()
        for (category in categories) {
            val categoryTransactions = transactions.filter { it.Category == category }
            val categoryAmount = categoryTransactions.sumOf { it.Amount }
            val percentage = if (totalAmount > 0) (categoryAmount / totalAmount * 100).toFloat() else 0f

            pieEntries.add(PieEntry(percentage, category))
        }

        // Create a PieDataSet
        val dataSet = pieDataSetFactory.createPieDataSet(pieEntries,"")
        dataSet.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${decimalFormat.format(value)}%" // Attach % to displayed values
            }
        })
        // Randomly select colors from the colorOptions list
        val selectedColors = ColorProvider.getColors(pieEntries.size)

        dataSet.colors = selectedColors

        return PieData(dataSet)
    }

    interface PieDataSetFactory {
        fun createPieDataSet(pieEntries: MutableList<PieEntry>, label:String): PieDataSet
    }

    //Factory to inject to circumvent PieDataSet implementation details for unit tests
    class DefaultPieDataSetFactory : PieDataSetFactory {
        override fun createPieDataSet(
            pieEntries: MutableList<PieEntry>,
            label: String
        ): PieDataSet {
            return PieDataSet(pieEntries,label)
        }
    }
}
