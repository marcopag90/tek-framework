package it.jbot.shared.util

import java.time.LocalDate
import java.time.ZoneId
import java.util.*

object JBotDateUtils {
    
    /**
     * Calculate if the given date is still valid, compared to a given _months_ parameter
     */
    fun isDateExpired(date: Date, months: Int): Boolean {
        
        return Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            .minusMonths(months.toLong())
            .isAfter(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
    }
    
    /**
     * Add given _months_ parameter to current date
     */
    
    fun addMonthsFromNow(months: Int): Date = Date.from(
        LocalDate.now().plusMonths(months.toLong()).atStartOfDay(
            ZoneId.systemDefault()
        ).toInstant()
    )
    
    //TODO check timestamp format and use it here
    /**
     * Utility function to evaluate timestamp where needed.
     *
     * Default implementation
     */
    @JvmStatic
    fun jbotTimestamp() : Date = Date()
}






