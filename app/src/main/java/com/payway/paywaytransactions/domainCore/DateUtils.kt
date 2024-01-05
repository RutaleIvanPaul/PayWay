package com.payway.paywaytransactions.domainCore

import com.payway.paywaytransactions.App
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Singleton to be used for formatting dates especially for display across the app
 */
object DateFormatter {
    // Use context.resources.configuration.locales[0] to get the current locale
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", App.getInstance().applicationContext.resources.configuration.locales[0])

    val displayDateFormat = SimpleDateFormat("dd MMM yyy", App.getInstance().applicationContext.resources.configuration.locales[0])

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
