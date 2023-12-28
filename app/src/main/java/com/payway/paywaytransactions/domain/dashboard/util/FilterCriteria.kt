package com.payway.paywaytransactions.domain.dashboard.util

import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction
import com.payway.paywaytransactions.domainCore.DateFormatter
import java.util.Date

class FilterCriteria(
    val type: String? = null,
    val minAmount: Float? = null,
    val maxAmount: Float? = null,
    val categories: List<String>? = null,
    val startDate: Date? = null,
    val endDate: Date? = null
) {
    fun getFilteredData(transactions: List<RemoteTransaction>): List<RemoteTransaction> {
        return transactions.filter { transaction ->
            (type.isNullOrBlank() || transaction.Type == type) &&
                    (minAmount == null || transaction.Amount >= minAmount) &&
                    (maxAmount == null || transaction.Amount <= maxAmount) &&
                    (categories.isNullOrEmpty() || transaction.Category in categories) &&
                    (startDate == null || DateFormatter.formatStringToDate(transaction.TxFinish) >= startDate) &&
                    (endDate == null || DateFormatter.formatStringToDate(transaction.TxFinish) <= endDate)
        }
    }
}