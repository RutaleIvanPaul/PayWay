package com.payway.paywaytransactions.app.dashboard.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.core.view.isVisible
import com.payway.paywaytransactions.App
import com.payway.paywaytransactions.R
import com.payway.paywaytransactions.databinding.ActivityMainBinding
import com.payway.paywaytransactions.databinding.FilterIconLayoutBinding
import com.payway.paywaytransactions.domain.dashboard.usecase.GetLineChartUseCase

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    val app = App.getInstance()
    val transactionsViewModel = app.transactionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Add toolbar widget to app bar
        setSupportActionBar(binding.toolbar)

        binding.customFilterIcon.setOnClickListener {
            binding.filterCard.isVisible = !binding.filterCard.isVisible
        }

        setupObservers()

    }

    override fun onResume() {
        super.onResume()
        transactionsViewModel.getTransactions()
    }

    private fun setupObservers() {
        //Observe LineData
        transactionsViewModel.linedata.observe(this) { linedata ->
            binding.linechart.xAxis.valueFormatter = GetLineChartUseCase.DateAxisFormatter()
            binding.linechart.data = linedata
            binding.linechart.setPinchZoom(true)
            binding.linechart.setScaleEnabled(true)
            binding.linechart.isHighlightPerDragEnabled = true
            binding.linechart.isHighlightPerTapEnabled = true

            binding.linechart.invalidate()//refresh
        }

        // Observe totalTransactions
        transactionsViewModel.totalTransactions.observe(this) { totalTransactions ->
            binding.totalTransactions.text = totalTransactions
        }

        // Observe totalAmount
        transactionsViewModel.totalAmount.observe(this) { totalAmount ->
            binding.totalAmount.text = totalAmount
        }

        // Observe withdrawAmount
        transactionsViewModel.withdrawAmount.observe(this) { withdrawAmount ->
            binding.withdrawAmount.text = withdrawAmount
        }

        // Observe depositAmount
        transactionsViewModel.depositAmount.observe(this) { depositAmount ->
            binding.depositAmount.text = depositAmount
        }
    }
}