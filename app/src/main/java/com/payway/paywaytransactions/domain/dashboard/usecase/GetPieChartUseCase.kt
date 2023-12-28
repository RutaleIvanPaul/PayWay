package com.payway.paywaytransactions.domain.dashboard.usecase

import android.graphics.Color
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction
import com.payway.paywaytransactions.domainCore.colorOptions
import kotlin.random.Random

class GetPieChartUseCase {

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
        val dataSet = PieDataSet(pieEntries, "Category Distribution by Amount")
        // Customize colors as needed
        // Randomly select colors from the colorOptions list
        val random = Random
        val selectedColors = colorOptions.shuffled(random).take(pieEntries.size)

        dataSet.colors = selectedColors

        return PieData(dataSet)
    }
}
