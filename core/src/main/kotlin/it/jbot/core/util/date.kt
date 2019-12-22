package it.jbot.core.util

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

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
 * Extension function to evaluate timestamp where needed.
 *
 * Default implementation
 */
fun Date.jbotTimestamp(): Date = this

@Configuration
class DateStringParser {

    val engRegDash = Regex("(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])-([12]\\d{3})")
    val enRegSlash = Regex("(0[1-9]|1[0-2])/(0[1-9]|[12]\\d|3[01])/([12]\\d{3})")
    val itRegDash = Regex("(0[1-9]|[12]\\d|3[01])-(0[1-9]|1[0-2])-([12]\\d{3})")
    val itRegSlash = Regex("(0[1-9]|[12]\\d|3[01])/(0[1-9]|1[0-2])/([12]\\d{3})")

    @Bean
    fun engRegDashFormat() = SimpleDateFormat("MM-dd-yyyy")

    @Bean
    fun engRegSlashFormat() = SimpleDateFormat("MM/dd/yyyy")

    @Bean
    fun itRegDashFormat() = SimpleDateFormat("dd-MM-yyyy")

    @Bean
    fun itRegSlashFormat() = SimpleDateFormat("dd/MM/yyyy")

    fun parse(dateString: String): Date? =
        when {
            dateString.matches(engRegDash) -> engRegDashFormat().parse(dateString)
            dateString.matches(enRegSlash) -> engRegSlashFormat().parse(dateString)
            dateString.matches(itRegDash) -> itRegDashFormat().parse(dateString)
            dateString.matches(itRegSlash) -> itRegSlashFormat().parse(dateString)
            else -> TODO()
        }
}
