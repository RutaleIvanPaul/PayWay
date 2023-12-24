package com.payway.paywaytransactions.app.dashboard.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.formatter.PercentFormatter
import com.payway.paywaytransactions.App
import com.payway.paywaytransactions.R
import com.payway.paywaytransactions.app.dashboard.presenter.TransactionsViewModel
import com.payway.paywaytransactions.app.dashboard.presenter.TransactionsViewModelFactory
import com.payway.paywaytransactions.databinding.ActivityMainBinding
import com.payway.paywaytransactions.domain.dashboard.usecase.GetTransactionsUseCase

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    val app = App.getInstance()
    val transactionsViewModel = app.transactionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()
        transactionsViewModel.getTransactions()
        transactionsViewModel.piedata.observe(this) { piedata ->
            binding.piechart.setData(piedata)
            //pie chart
            piedata.setValueFormatter(PercentFormatter())
            piedata.setValueTextSize(11f)
            binding.piechart.setUsePercentValues(true)
            binding.piechart.description.isEnabled = false
            binding.piechart.setExtraOffsets(5F, 10F, 5F, 5F)

            binding.piechart.dragDecelerationFrictionCoef = 0.95f

            binding.piechart.setTransparentCircleAlpha(110)

            binding.piechart.holeRadius = 58f
            binding.piechart.transparentCircleRadius = 61f

            binding.piechart.setDrawCenterText(true)

            binding.piechart.rotationAngle = 0.toFloat()
            // enable rotation of the chart by touch
            binding.piechart.isRotationEnabled = true
            binding.piechart.isHighlightPerTapEnabled = true
            binding.piechart.invalidate() // refresh
        }
    }
}