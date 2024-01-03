package com.payway.paywaytransactions.app

import android.graphics.Color
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.RadarData
import com.payway.paywaytransactions.app.dashboard.presenter.TransactionsViewModel
import com.payway.paywaytransactions.data.dashboard.model.MyResult
import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction
import com.payway.paywaytransactions.domain.dashboard.usecase.GetLineChartUseCase
import com.payway.paywaytransactions.domain.dashboard.usecase.GetPieChartUseCase
import com.payway.paywaytransactions.domain.dashboard.usecase.GetRadarChartUseCase
import com.payway.paywaytransactions.domain.dashboard.usecase.GetTransactionsUseCase
import com.payway.paywaytransactions.domainCore.ColorProvider
import io.mockk.coEvery
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
}