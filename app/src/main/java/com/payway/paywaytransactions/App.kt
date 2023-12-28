package com.payway.paywaytransactions

import android.app.Application
import com.payway.paywaytransactions.app.dashboard.presenter.TransactionsViewModel
import com.payway.paywaytransactions.data.dashboard.TransactionsAPI
import com.payway.paywaytransactions.data.dashboard.repository.TransactionsRemoteDataSource
import com.payway.paywaytransactions.domain.dashboard.usecase.GetLineChartUseCase
import com.payway.paywaytransactions.domain.dashboard.usecase.GetPieChartUseCase
import com.payway.paywaytransactions.domain.dashboard.usecase.GetRadarChartUseCase
import com.payway.paywaytransactions.domain.dashboard.usecase.GetTransactionsUseCase
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class App: Application() {

    private val transactionsAPI: TransactionsAPI = createTransactionsAPI()
    private val transactionsRemoteDataSource: TransactionsRemoteDataSource =
        TransactionsRemoteDataSource(transactionsAPI)
    private val getTransactionsUseCase: GetTransactionsUseCase =
        GetTransactionsUseCase(transactionsRemoteDataSource)

    private val getLineChartUseCase: GetLineChartUseCase =
        GetLineChartUseCase()

    private val getPieChartUseCase:GetPieChartUseCase =
        GetPieChartUseCase()

    private val getRadarChartUseCase:GetRadarChartUseCase =
        GetRadarChartUseCase()

    val transactionsViewModel: TransactionsViewModel =
        TransactionsViewModel(getTransactionsUseCase,getLineChartUseCase,getPieChartUseCase,getRadarChartUseCase)

    private fun createTransactionsAPI(): TransactionsAPI =
        createRetrofit().create(TransactionsAPI::class.java)

    private fun createRetrofit(): Retrofit {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Set the desired log level
        }

        return Retrofit.Builder()
            .baseUrl("https://45877c7d-518f-4fb9-9ed5-382027346e41.mock.pstmn.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(loggingInterceptor).build())
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private lateinit var instance: App

        fun getInstance(): App {
            return instance
        }
    }
}

