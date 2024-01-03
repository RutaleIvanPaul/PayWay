package com.payway.paywaytransactions.data
import com.payway.paywaytransactions.data.dashboard.TransactionsAPI
import com.payway.paywaytransactions.data.dashboard.model.MyResult
import com.payway.paywaytransactions.data.dashboard.repository.TransactionsRemoteDataSource
import com.payway.paywaytransactions.expectedTransactions
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TransactionsRemoteDataSourceTest {

    // Mocks
    private lateinit var transactionsAPI: TransactionsAPI
    private lateinit var remoteDataSource: TransactionsRemoteDataSource


    private val expectedException = RuntimeException("Sample error")

    @Before
    fun setUp() {
        // Initialize mocks
        transactionsAPI = mockk()
        remoteDataSource = TransactionsRemoteDataSource(transactionsAPI)
    }

    @Test
    fun `getTransactions should return Success with transactions when API call is successful`() {
        coEvery { transactionsAPI.getTransactions() } returns expectedTransactions

        val result = runBlocking { remoteDataSource.getTransactions() }

        assertEquals(expectedTransactions, (result as MyResult.Success).data)
    }

    // Test case 2: Test error handling when API call fails
    @Test
    fun `getTransactions should return Error when API call fails`() {
        coEvery { transactionsAPI.getTransactions() } throws expectedException

        val result = runBlocking { remoteDataSource.getTransactions() }

        assertEquals(expectedException, (result as MyResult.Error).exception)
    }

    @Test
    fun `Verify details of sample transactions` () {
        coEvery { transactionsAPI.getTransactions() } returns expectedTransactions

        val result = runBlocking { remoteDataSource.getTransactions() }

        val transactions = (result as MyResult.Success).data


        assertEquals(2, transactions.size)

        transactions[0].apply {
            assertEquals(1000.0, Amount,0.0)
            assertEquals("Internet", Category)
            assertEquals("Airtel Internet", Service)
            assertEquals("Deposit", Type)
            assertEquals("2023-10-29 14:07:02", TxFinish)
        }

        transactions[1].apply {
            assertEquals(23000.0, Amount,0.0)
            assertEquals("Mobile Money", Category)
            assertEquals("Airtel Money", Service)
            assertEquals("Deposit", Type)
            assertEquals("2023-10-29 14:09:40", TxFinish)
        }
    }


}
