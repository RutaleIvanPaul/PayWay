package com.payway.paywaytransactions.domain.dashboard.usecase

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction
import com.payway.paywaytransactions.domainCore.ColorProvider

/**Use Case concerned with the logic around taking transactions and preparing them for
 * display in the Radar chart.
 **/
class GetRadarChartUseCase {
    companion object{
        var categoryLabels:Array<String> = arrayOf()
    }
    /**
     *
     *Takes transactions, processes categories, splits transactions into deposits and withdraws
     * to create entries for the radar chart
     *
     */
    fun execute(transactions: List<RemoteTransaction>):RadarData{
        //get distinct categories for the axes
        val categories = transactions.map { it.Category }.distinct()
        categoryLabels = categories.toTypedArray()
        //we will plot deposits vs withdraws
        val radarEntriesDeposit = mutableListOf<RadarEntry>()
        val radarEntriesWithdraw = mutableListOf<RadarEntry>()

        //get sums of amounts for each category
        //attach to a radar entry
        categories.forEach { category ->
            val sumAmountDeposit = transactions
                .filter { it.Category == category && it.Type == "Deposit" }
                .sumOf { it.Amount }
                .toFloat()

            val sumAmountWithdraw = transactions
                .filter { it.Category == category && it.Type == "Withdraw" }
                .sumOf { it.Amount }
                .toFloat()

            radarEntriesDeposit.add(RadarEntry(sumAmountDeposit, category))
            radarEntriesWithdraw.add(RadarEntry(sumAmountWithdraw, category))
        }

        //style and setup radar chart
        val radarDataSetDeposit = RadarDataSet(radarEntriesDeposit, "Deposits")
        radarDataSetDeposit.setDrawValues(false)
        val depositsColor = ColorProvider.getNextColor()
        radarDataSetDeposit.color = depositsColor
        radarDataSetDeposit.fillColor = depositsColor
        radarDataSetDeposit.valueTextSize = 10f

        val radarDataSetWithdraw = RadarDataSet(radarEntriesWithdraw, "Withdraws")
        radarDataSetWithdraw.setDrawValues(false)
        val withdrawsColor = ColorProvider.getNextColor()
        radarDataSetWithdraw.color = withdrawsColor
        radarDataSetWithdraw.fillColor = withdrawsColor
        radarDataSetWithdraw.valueTextSize = 10f

        val radarDataSets: List<IRadarDataSet> = listOf(radarDataSetDeposit, radarDataSetWithdraw)

        return RadarData(radarDataSets)
    }

    /**
     * To format the large amount values displayed on the radar chart
     */
    class LargeValueFormatter : ValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return formatValue(value)
        }

        override fun getFormattedValue(value: Float): String {
            return formatValue(value)
        }

        private fun formatValue(value: Float): String {
            val suffixes = arrayOf("", "K", "M", "B", "T")
            var num = value
            var index = 0

            //move along the suffixes in steps of 1000 to attach the proper suffix to the figure.
            while (num >= 1000 && index < suffixes.size - 1) {
                num /= 1000
                index++
            }

            return "${"%.1f".format(num)}${suffixes[index]}"
        }
    }

}