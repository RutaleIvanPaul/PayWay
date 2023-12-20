package com.payway.paywaytransactions.app.dashboard.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
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
        transactionsViewModel.transactions.observe(this, { transactions ->
            binding.transactiontxt.text = transactions.toString()
        })
    }
}