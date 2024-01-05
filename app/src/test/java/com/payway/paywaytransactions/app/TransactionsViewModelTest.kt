package com.payway.paywaytransactions.app

import android.graphics.Color
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.RadarData
import com.payway.paywaytransactions.app.dashboard.presenter.TransactionsViewModel
import com.payway.paywaytransactions.domain.dashboard.usecase.GetLineChartUseCase
import com.payway.paywaytransactions.domain.dashboard.usecase.GetPieChartUseCase
import com.payway.paywaytransactions.domain.dashboard.usecase.GetRadarChartUseCase
import com.payway.paywaytransactions.domain.dashboard.usecase.GetTransactionsUseCase
import com.payway.paywaytransactions.domainCore.ColorProvider
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class TransactionsViewModelTest {
    // Rule for testing LiveData with Architecture Components
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testCoroutineDispatcher = TestCoroutineDispatcher()


    private lateinit var transactionsViewModel:TransactionsViewModel
    private val getTransactionsUseCaseMock: GetTransactionsUseCase = mockk()
    private val getLineChartUseCaseMock: GetLineChartUseCase = mockk()
    private val getPieChartUseCaseMock: GetPieChartUseCase = mockk()
    private val getRadarChartUseCaseMock: GetRadarChartUseCase = mockk()


    // Mock LiveData Observers
    private val lineDataObserver: Observer<LineData> = spyk()
    private val pieDataObserver: Observer<PieData> = spyk()
    private val radarDataObserver: Observer<RadarData> = spyk()

    @Before
    fun setUp(){
        transactionsViewModel =
            TransactionsViewModel(getTransactionsUseCaseMock,getLineChartUseCaseMock,
                getPieChartUseCaseMock,getRadarChartUseCaseMock)

        Dispatchers.setMain(testCoroutineDispatcher)

        transactionsViewModel.linedata.observeForever(lineDataObserver)
        transactionsViewModel.pieData.observeForever(pieDataObserver)
        transactionsViewModel.radarData.observeForever(radarDataObserver)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
        unmockkAll()
    }

    @Test
    fun `test getTransactions updates transactions livedata`() = testCoroutineDispatcher.runBlockingTest {
        // Arrange
        mockkObject(ColorProvider)
        every { ColorProvider.getNextColor() } returns Color.BLUE // Mock the color to be returned
        every { getLineChartUseCaseMock.execute(any()) } returns LineData() // Mock the LineChartUseCase response

        // Act
        transactionsViewModel.getLineChart(emptyList(), "Label") // Call the function that updates linedata

        // Assert
        coVerify { getLineChartUseCaseMock.execute(any()) } // Verify that the LineChartUseCase was called

        // Verify that linedata was updated with LineData
        verify { lineDataObserver.onChanged(any()) }
        confirmVerified(lineDataObserver)
    }


    @Test
    fun `test getPieChart updates pieData livedata`() = testCoroutineDispatcher.runBlockingTest {
        // Arrange
        every { getPieChartUseCaseMock.execute(any()) } returns PieData() // Mock the PieChartUseCase response

        // Act
        transactionsViewModel.getPieChart(emptyList()) // Call the function that updates pieData

        // Assert
        coVerify { getPieChartUseCaseMock.execute(any()) } // Verify that the PieChartUseCase was called

        // Verify that pieData was updated with PieData
        verify { pieDataObserver.onChanged(any()) }
        confirmVerified(pieDataObserver)
    }

    @Test
    fun `test getRadarChart updates radarData livedata`() = testCoroutineDispatcher.runBlockingTest {
        // Arrange
        every { getRadarChartUseCaseMock.execute(any()) } returns RadarData() // Mock the RadarChartUseCase response

        // Act
        transactionsViewModel.getRadarChart(emptyList()) // Call the function that updates radarData

        // Assert
        coVerify { getRadarChartUseCaseMock.execute(any()) } // Verify that the RadarChartUseCase was called

        // Verify that radarData was updated with RadarData
        verify { radarDataObserver.onChanged(any()) }
        confirmVerified(radarDataObserver)
    }
}