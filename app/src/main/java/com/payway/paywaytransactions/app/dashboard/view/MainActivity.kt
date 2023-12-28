package com.payway.paywaytransactions.app.dashboard.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
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
        // Inflate the custom icon layout
        val customIconLayout = LayoutInflater.from(this).inflate(R.layout.filter_icon_layout, null)
        // Find the ImageView in the custom layout
        val customIcon = customIconLayout.findViewById<ImageView>(R.id.customFilterIcon)
        // Set a click listener for custom filter icon
        customIcon.setOnClickListener {

        }

        // Add the custom layout as the action layout for a menu item
        binding.toolbar.addView(customIconLayout)

    }

    override fun onResume() {
        super.onResume()
        transactionsViewModel.getTransactions()
        transactionsViewModel.linedata.observe(this) { linedata ->
            binding.linechart.xAxis.valueFormatter = GetLineChartUseCase.DateAxisFormatter()
            binding.linechart.data = linedata
            binding.linechart.setPinchZoom(true)
            binding.linechart.setScaleEnabled(true)
            binding.linechart.isHighlightPerDragEnabled = true
            binding.linechart.isHighlightPerTapEnabled = true

            binding.linechart.invalidate()//refresh
        }
    }
}