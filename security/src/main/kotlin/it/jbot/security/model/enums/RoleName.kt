package it.jbot.security.model.enums

import it.jbot.core.exception.JBotServiceException
import it.jbot.core.exception.ServiceExceptionData
import it.jbot.core.util.LabelEnum
import it.jbot.security.i18n.SecurityMessageSource
import it.jbot.security.i18n.SecurityMessageSource.Companion.errorRoleNotFound
import org.springframework.http.HttpStatus

enum class RoleName(
    override val label: String
) : LabelEnum {

    ROLE_ADMIN("Administrator"),
    ROLE_USER("User");

    companion object {

        fun fromString(name: String): RoleName {

            val messageSource = SecurityMessageSource()

            return when (name) {

                ROLE_ADMIN.name -> ROLE_ADMIN
                ROLE_USER.name -> ROLE_USER

                else -> throw JBotServiceException(
                    data = ServiceExceptionData(
                        source = messageSource,
                        message = errorRoleNotFound,
                        parameters = arrayOf(name)
                    ),
                    httpStatus = HttpStatus.NOT_ACCEPTABLE
                )
            }
        }
    }
}