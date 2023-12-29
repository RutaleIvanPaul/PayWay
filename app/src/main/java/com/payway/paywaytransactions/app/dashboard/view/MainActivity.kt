package com.payway.paywaytransactions.app.dashboard.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.MultiAutoCompleteTextView
import android.widget.SeekBar
import androidx.core.view.isVisible
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.payway.paywaytransactions.App
import com.payway.paywaytransactions.R
import com.payway.paywaytransactions.databinding.ActivityMainBinding
import com.payway.paywaytransactions.databinding.FilterIconLayoutBinding
import com.payway.paywaytransactions.domain.dashboard.usecase.GetLineChartUseCase
import com.payway.paywaytransactions.domain.dashboard.usecase.GetRadarChartUseCase
import com.payway.paywaytransactions.domain.dashboard.util.FilterCriteria
import com.payway.paywaytransactions.domainCore.DateFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    val app = App.getInstance()
    val transactionsViewModel = app.transactionsViewModel

    private val filterCriteria = FilterCriteria()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Add toolbar widget to app bar
        setSupportActionBar(binding.toolbar)

        setupClickListeners()
        setupObservers()

    }

    private fun setupClickListeners() {

        binding.customFilterIcon.setOnClickListener {
            binding.filterCard.isVisible = !binding.filterCard.isVisible
        }

        binding.startDateButton.setOnClickListener {
            showDatePickerDialog(it as Button,true)
        }

        binding.endDateButton.setOnClickListener {
            showDatePickerDialog(it as Button,false)
        }

        binding.clearFilters.setOnClickListener {
            binding.clearFilters.visibility = View.GONE
            //reset filters and fetch all data
        }

        binding.applyFiltersButton.setOnClickListener {
            binding.clearFilters.visibility = View.VISIBLE
            //use filter criteria to select data
            //get selected categories
            filterCriteria.categories.addAll(binding.categoriesSpinner.text.toString()
                .split(",").map { it.trim()}.filter { it.isNotEmpty() })
            transactionsViewModel.getFilteredTransactions(filterCriteria)
        }
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

        //Observe PieData
        transactionsViewModel.pieData.observe(this){piedata ->
            binding.piechart.data = piedata
            binding.piechart.invalidate()
        }

        //Observe Radar Data
        transactionsViewModel.radarData.observe(this){radardata ->
            binding.radarchart.xAxis.valueFormatter = IndexAxisValueFormatter(GetRadarChartUseCase.categoryLabels)
            binding.radarchart.yAxis.valueFormatter = GetRadarChartUseCase.LargeValueFormatter()
            binding.radarchart.data = radardata
            binding.radarchart.invalidate()
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

        //Observe Distinct Categories
        transactionsViewModel.distinctCategories.observe(this){distinctCategories ->
            binding.categoriesSpinner.setAdapter(ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,distinctCategories))
            binding.categoriesSpinner.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
            binding.categoriesSpinner.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    // Show the dropdown when the MultiAutoCompleteTextView is clicked
                    binding.categoriesSpinner.showDropDown()
                }
                false
            }
        }

        //Observe Distinct Types
        transactionsViewModel.distinctTypes.observe(this){distinctTypes ->
            val typesList = distinctTypes.toMutableList()
            typesList.add(0,getString(R.string.spinner_prompt))
            binding.typeSpinner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,typesList)
            binding.typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    if (position > 0) {
                        filterCriteria.type = p0?.getItemAtPosition(position).toString()
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
        }

        //Observe minmax values
        transactionsViewModel.seekBarMinMax.observe(this){minmax ->
            binding.minAmountSeekBar.max = (minmax.second - minmax.first).toInt()
            binding.maxAmountSeekBar.max = (minmax.second - minmax.first).toInt()

            binding.minAmountSeekBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, p2: Boolean) {
                    binding.minAmountSeekBarValue.text = progress.toString()
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                    binding.seekBarValues.visibility = View.VISIBLE
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    filterCriteria.minAmount = seekBar?.progress?.toDouble()
                }

            })

            binding.maxAmountSeekBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, p2: Boolean) {
                    binding.maxAmountSeekBarValue.text = progress.toString()
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                    binding.seekBarValues.visibility = View.VISIBLE
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    filterCriteria.maxAmount = seekBar?.progress?.toDouble()
                }

            })
        }
    }

    private fun showDatePickerDialog(clickedButton: Button,isStartDate:Boolean) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view: DatePicker, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                updateSelectedDate(selectedDate.time, clickedButton,isStartDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun updateSelectedDate(date: Date,clickedButton: Button, isStartDate:Boolean) {
        filterCriteria.apply {
            if (isStartDate)
                startDate = date
            else
                endDate = date
        }
        val formattedDate = DateFormatter.displayDateFormat.format(date)
        clickedButton.text = "$formattedDate"
    }
}