package com.payway.paywaytransactions.domainCore

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateFormatter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    val displayDateFormat = SimpleDateFormat("dd MMM yyy", Locale.getDefault())

    fun formatStringToDate(dateString: String): Date {
        return dateString.let {
            try {
                dateFormat.parse(it)
            } catch (e: ParseException) {
                e.printStackTrace()
                Date()
            }
        }
    }

    fun formatDateStringToFloat(dateString: String): Float {
        return dateString.let {
            try {
                val date = dateFormat.parse(it)
                val calendar = Calendar.getInstance()
                calendar.time = date

                // Truncate time details
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                // Convert to float
                calendar.timeInMillis.toFloat()
            } catch (e: ParseException) {
                e.printStackTrace()
                0F
            }
        }
    }

}
