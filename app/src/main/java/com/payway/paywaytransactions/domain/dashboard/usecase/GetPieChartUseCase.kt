package com.payway.paywaytransactions.domain.dashboard.usecase

import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction
import com.payway.paywaytransactions.domainCore.ColorProvider
import com.payway.paywaytransactions.domainCore.decimalFormat

/**Use Case concerned with the logic around taking transactions and preparing them for
* display in the Pie chart.
 **/
class GetPieChartUseCase(
    private val pieDataSetFactory: PieDataSetFactory
) {
    /**
     * Takes transactions, processes categories and
     * their percentage amount vs other transactions
     * to create entries for the pie chart
     */
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
        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "${decimalFormat.format(value)}%" // Attach % to displayed values
            }
        }
        // Randomly select colors from the colorOptions list
        val selectedColors = ColorProvider.getColors(pieEntries.size)

        dataSet.colors = selectedColors

        return PieData(dataSet)
    }

    /**
     * Define the Factory interface to decouple the creation of PieDataSet from GetPieChartUseCase
     */
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
