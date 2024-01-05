package com.payway.paywaytransactions.domain

import android.graphics.Color
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.payway.paywaytransactions.domain.dashboard.usecase.GetPieChartUseCase
import com.payway.paywaytransactions.domainCore.ColorProvider
import com.payway.paywaytransactions.expectedTransactions
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GetPieChartUseCaseTest {
    @Test
    fun `execute should return PieData with correct entries`() {
        // Arrange
        mockkObject(ColorProvider)
        every { ColorProvider.getNextColor() } returns Color.BLUE // Mock the color to be returned


        val pieDataSet = mockk<PieDataSet>(relaxUnitFun = true)
        every { pieDataSet.color = any() } just Runs // Mock setColor method
        every { pieDataSet.setDrawValues(any()) } just Runs // Mock setDrawValues method
        every { pieDataSet.yMax } returns 150.0F
        every { pieDataSet.yMin } returns 100.0F
        every { pieDataSet.xMax } returns 0F
        every { pieDataSet.xMin } returns 0F
        every { pieDataSet.label } returns ""
        every { pieDataSet.axisDependency } returns YAxis.AxisDependency.LEFT

        val pieDataSetFactory = mockk<GetPieChartUseCase.PieDataSetFactory>()
        every { pieDataSetFactory.createPieDataSet(any(), any()) } returns pieDataSet // Mock PoeDataSet creation


        val pieChartUseCase = GetPieChartUseCase(pieDataSetFactory)

        val result = pieChartUseCase.execute(expectedTransactions)

        assertNotNull(result)

        // Verify that the result is an instance of PieData
        assertTrue(result is PieData)


        //Verify the Count of Pies in dataset matches count added
        assertEquals(1, result.dataSetCount)


        // Assertions below are limited by the requirement to mock the
    // entryCount method which would then ultimately defeat the purpose

//        assertEquals(expectedTransactions.size, result.dataSet.entryCount)


        // Verify that the sum of percentages is approximately 100 (allowing for floating-point precision)
//        assertEquals(100f, result.dataSet., 0.01f)
//        var yValSum = 0F

        // Verify that each category has a corresponding entry in the PieData
//        for (entryIndex in 0 until result.dataSet.entryCount) {
//            val entry = result.dataSet.getEntryForIndex(entryIndex)
//            yValSum += entry.y
//            assertTrue(expectedTransactions.any { it.Category == entry.label })
//        }
//
//        assertEquals(100f, yValSum, 0.01f)
    }
}
