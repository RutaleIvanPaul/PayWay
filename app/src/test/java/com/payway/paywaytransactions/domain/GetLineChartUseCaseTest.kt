package com.payway.paywaytransactions.domain

import android.graphics.Color
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.payway.paywaytransactions.domain.dashboard.model.LineDefinition
import com.payway.paywaytransactions.domain.dashboard.usecase.GetLineChartUseCase
import com.payway.paywaytransactions.domainCore.ColorProvider
import com.payway.paywaytransactions.expectedTransactions
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetLineChartUseCaseTest {

    val lineDefinition1 = LineDefinition(
        filteredList = expectedTransactions,
        color = 0xFF0000,
        label = "Label1"
    )

    val lineDefinition2 = LineDefinition(
        filteredList = expectedTransactions,
        color = 0x00FF00,
        label = "Label2"
    )

    val lineDefinitions = listOf(lineDefinition1, lineDefinition2)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `execute should return LineData with correct entries`() {
        mockkObject(ColorProvider)
        every { ColorProvider.getNextColor() } returns Color.BLUE // Mock the color to be returned

        val lineDataSet = mockk<LineDataSet>(relaxUnitFun = true)
        every { lineDataSet.setColor(any()) } just Runs // Mock setColor method
        every { lineDataSet.setDrawValues(any()) } just Runs // Mock setDrawValues method
        every { lineDataSet.yMax } returns 150.0F
        every { lineDataSet.yMin } returns 100.0F
        every { lineDataSet.xMax } returns 0F
        every { lineDataSet.xMin } returns 0F
        every { lineDataSet.label } returns ""
        every { lineDataSet.axisDependency } returns YAxis.AxisDependency.LEFT

        val lineDataSetFactory = mockk<GetLineChartUseCase.LineDataSetFactory>()
        every { lineDataSetFactory.createLineDataSet(any(), any()) } returns lineDataSet // Mock LineDataSet creation

        val useCase = GetLineChartUseCase(lineDataSetFactory) // Provide LineDataSetFactory directly

        val result = useCase.execute(lineDefinitions)
        // Verify that the result is an instance of LineData
        assertTrue(result is LineData)

        // Assert
        //Verify the Count of Lines in dataset matches count added
        assertEquals(2, result.dataSetCount)
    }

    class TestLineDataSetFactory : GetLineChartUseCase.LineDataSetFactory {
        override fun createLineDataSet(yVals: List<Entry>, label: String): LineDataSet {
            return mockk()
        }
    }

}
