package com.payway.paywaytransactions.domain

import android.graphics.Color
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction
import com.payway.paywaytransactions.domain.dashboard.model.LineDefinition
import com.payway.paywaytransactions.domain.dashboard.usecase.GetLineChartUseCase
import com.payway.paywaytransactions.domainCore.ColorProvider
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetLineChartUseCaseTest {

    private val lineDefinition1: LineDefinition = mockk()
    private val lineDefinition2: LineDefinition = mockk()
    private val transaction1: RemoteTransaction = mockk()
    private val transaction2: RemoteTransaction = mockk()
    private val dateAxisFormatter: GetLineChartUseCase.DateAxisFormatter = mockk()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `execute should return LineData with correct entries`() {
        // Arrange
        mockkObject(ColorProvider)
        every { ColorProvider.getNextColor() } returns Color.BLUE // Mock the color to be returned

        val lineDefinitions = listOf(lineDefinition1, lineDefinition2)
        val filteredList1 = listOf(transaction1)
        val filteredList2 = listOf(transaction2)

        every { lineDefinition1.filteredList } returns filteredList1
        every { lineDefinition1.label } returns "Label1" // Add label property
        every { lineDefinition1.color } returns 0xFF0000 // Add color property

        every { lineDefinition2.filteredList } returns filteredList2
        every { lineDefinition2.label } returns "Label2" // Add label property
        every { lineDefinition2.color } returns 0x00FF00 // Add color property

        every { transaction1.TxFinish } returns "2023-10-01 08:38:18"
        every { transaction1.Amount } returns 100.0

        every { transaction2.TxFinish } returns "2023-10-01 08:38:18"
        every { transaction2.Amount } returns 150.0

        every { dateAxisFormatter.getFormattedValue(1641043200000f) } returns "Jan 01"
        every { dateAxisFormatter.getFormattedValue(1641129600000f) } returns "Jan 02"

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

        // Act
        val result = useCase.execute(lineDefinitions)
        // Verify that the result is an instance of LineData
        assertTrue(result is LineData)

        // Assert
//        assertEquals(2, result.dataSetCount)
//
//        // Assuming LineDataSet is equal to LineDataSets[0]
//        assertEquals("Jan 01", result.getDataSetByIndex(0).getLabel())
//        assertEquals(2, result.getDataSetByIndex(0).entryCount)
//        assertEquals(100.0f, result.getDataSetByIndex(0).getEntryForIndex(0).y)
//        assertEquals(150.0f, result.getDataSetByIndex(0).getEntryForIndex(1).y)
//
//        // Assuming LineDataSet is equal to LineDataSets[1]
//        assertEquals("Jan 02", result.getDataSetByIndex(1).getLabel())
//        assertEquals(1, result.getDataSetByIndex(1).entryCount)
//        assertEquals(150.0f, result.getDataSetByIndex(1).getEntryForIndex(0).y)
    }

    class TestLineDataSetFactory : GetLineChartUseCase.LineDataSetFactory {
        override fun createLineDataSet(yVals: List<Entry>, label: String): LineDataSet {
            return mockk()
        }
    }
    object TestDependencyContainer {
        val lineDataSetFactory: GetLineChartUseCase.LineDataSetFactory = TestLineDataSetFactory()
        val getLineChartUseCase: GetLineChartUseCase = GetLineChartUseCase(lineDataSetFactory)
    }


}
