package com.payway.paywaytransactions.domain.dashboard.util

import com.payway.paywaytransactions.data.dashboard.model.RemoteTransaction
import com.payway.paywaytransactions.domainCore.DateFormatter
import java.util.Date

/**
 * Filter Criteria to be used to create a well structured but more importantly generic object that will consistenly define
 * filtering parameters for the Remote Transactions.
 */
class FilterCriteria(
    var type: String? = null,
    var minAmount: Double? = null,
    var maxAmount: Double? = null,
    val categories: ArrayList<String> = arrayListOf(),
    var startDate: Date? = null,
    var endDate: Date? = null
) {
    /**Take a transactions list and follows the filter criteria object to return accordingly**/
    fun getFilteredData(transactions: List<RemoteTransaction>): List<RemoteTransaction> {
        return transactions.filter { transaction ->
            (type.isNullOrBlank() || transaction.Type == type) &&
                    (minAmount == null || transaction.Amount >= minAmount as Double) &&
                    (maxAmount == null || transaction.Amount <= maxAmount as Double) &&
                    (categories.isNullOrEmpty() || transaction.Category in categories) &&
                    (startDate == null || DateFormatter.formatStringToDate(transaction.TxFinish) >= startDate) &&
                    (endDate == null || DateFormatter.formatStringToDate(transaction.TxFinish) <= endDate)
        }
    }

    fun reset() {
        startDate = null
        endDate = null
        type = null
        categories.clear()
        minAmount = null
        maxAmount = null
    }
}