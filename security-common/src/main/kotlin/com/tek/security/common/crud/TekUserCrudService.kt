package com.tek.security.common.crud

import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekServiceException
import com.tek.core.i18n.CoreMessageSource
import com.tek.core.service.TekWritableCrudService
import com.tek.core.util.isFalse
import com.tek.core.util.isTrue
import com.tek.core.util.orNull
import com.tek.security.common.SECURITY_MESSAGE_SOURCE
import com.tek.security.common.SECURITY_VALIDATOR
import com.tek.security.common.form.UserCreateForm
import com.tek.security.common.form.UserUpdateForm
import com.tek.security.common.i18n.SecurityMessageBundle
import com.tek.security.common.model.QTekProfile
import com.tek.security.common.model.QTekUser
import com.tek.security.common.model.TekProfile
import com.tek.security.common.model.TekUser
import com.tek.security.common.repository.TekPreferencesRepository
import com.tek.security.common.repository.TekProfileRepository
import com.tek.security.common.repository.TekUserRepository
import com.tek.security.common.service.TekAuthService
import com.tek.security.common.service.TekProfileService
import com.tek.security.common.service.TekRegisterService
import com.tek.security.common.service.TekTokenService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDate
import javax.validation.Validator

@Service
class TekUserCrudService(
    @Qualifier(SECURITY_MESSAGE_SOURCE) val securityMessageSource: MessageSource,
    @Qualifier(SECURITY_VALIDATOR) override val validator: Validator,
    val authService: TekAuthService,
    val tokenService: TekTokenService,
    val profileService: TekProfileService,
    val registerService: TekRegisterService,
    val profileRepository: TekProfileRepository,
    val preferencesRepository: TekPreferencesRepository
) : TekWritableCrudService<TekUser, Long, TekUserRepository, UserCreateForm, UserUpdateForm>(
    QTekUser.tekUser.id.stringValue(),
    TekUser::class,
    validator
) {

    override fun new(form: UserCreateForm): TekUser {
        val user = super.new(form)
        registerService.isUsernameAlreadyTaken(form.username)
        form.profiles.forEach { profile -> profileService.checkIfExists(profile) }
        user.password = authService.passwordEncoder().encode(form.username)
        user.pwdExpireAt = LocalDate.now()
        user.enabled = true
        return user
    }

    override fun beforeUpdate(properties: MutableMap<String, Any?>, entity: TekUser): TekUser {
        val oldUsername = entity.username
        if (properties.containsKey(TekUser::username.name)) {
            val username = properties[TekUser::username.name] as String?
            if (username.isNullOrBlank()) {
                throw TekServiceException(
                    data = ServiceExceptionData(
                        source = coreMessageSource,
                        message = CoreMessageSource.errorEmptyField,
                        parameters = arrayOf(TekUser::username.name)
                    ),
                    httpStatus = HttpStatus.BAD_REQUEST
                )
            }
            if (entity.username != username)
                repository.existsByUsername(username).isTrue {
                    throw TekServiceException(
                        data = ServiceExceptionData(
                            source = securityMessageSource,
                            message = SecurityMessageBundle.errorConflictUsername,
                            parameters = arrayOf(username)
                        ),
                        httpStatus = HttpStatus.CONFLICT
                    )
                }
            entity.username = username
        }
        if (properties.containsKey(TekUser::password.name)) {
            val password = properties[TekUser::password.name] as String?

            if (password.isNullOrBlank()) {
                throw TekServiceException(
                    data = ServiceExceptionData(
                        source = coreMessageSource,
                        message = CoreMessageSource.errorEmptyField,
                        parameters = arrayOf(TekUser::password.name)
                    ),
                    httpStatus = HttpStatus.BAD_REQUEST
                )
            }
            authService.isValidPassword(password).isFalse {
                throw TekServiceException(
                    data = ServiceExceptionData(
                        source = securityMessageSource,
                        message = SecurityMessageBundle.errorNotValidPassword
                    ),
                    httpStatus = HttpStatus.BAD_REQUEST
                )
            }
            entity.password = authService.passwordEncoder().encode(password)
        }
        if (properties.containsKey(TekUser::email.name)) {
            val email = properties[TekUser::email.name] as String?

            if (email.isNullOrBlank()) {
                throw TekServiceException(
                    data = ServiceExceptionData(
                        source = coreMessageSource,
                        message = CoreMessageSource.errorEmptyField,
                        parameters = arrayOf(TekUser::email.name)
                    ),
                    httpStatus = HttpStatus.BAD_REQUEST
                )
            }
            if (entity.email != email)
                repository.existsByEmail(email).isTrue {
                    throw TekServiceException(
                        data = ServiceExceptionData(
                            source = securityMessageSource,
                            message = SecurityMessageBundle.errorConflictEmail,
                            parameters = arrayOf(email)
                        ),
                        httpStatus = HttpStatus.CONFLICT
                    )
                }
            entity.email = email
        }

        if (properties.containsKey(TekUser::userExpireAt.name)) {
            val userExpireAt = properties[TekUser::userExpireAt.name] as LocalDate?
            entity.userExpireAt = userExpireAt
        }

        if (properties.containsKey(TekUser::enabled.name)) {
            val enabled = properties[TekUser::enabled.name] as Boolean?
                ?: throw TekServiceException(
                    data = ServiceExceptionData(
                        source = coreMessageSource,
                        message = CoreMessageSource.errorEmptyField,
                        parameters = arrayOf(TekUser::enabled.name)
                    ),
                    httpStatus = HttpStatus.BAD_REQUEST
                )
            entity.enabled = enabled
        }

        if (properties.containsKey(TekUser::profiles.name)) {
            val profiles = mutableSetOf<TekProfile>()

            @Suppress("unchecked_cast")
            val profileIds = properties[TekUser::profiles.name] as MutableList<Long>
            for (profileId in profileIds) {
                profileRepository.findOne(QTekProfile.tekProfile.id.eq(profileId)).orNull()?.let {
                    profiles.add(it)
                }
            }
            properties[TekUser::profiles.name] = profiles
            log.info("User profiles changed! Searching for user tokens...")
            tokenService.invalidateUserTokens(oldUsername!!)
        }

        val (isAcceptable, constraintMessage) = authService.checkPasswordConstraints(
            entity.username!!,
            entity.email,
            entity.password!!
        )
        if (!isAcceptable) {
            throw TekServiceException(
                data = ServiceExceptionData(
                    source = securityMessageSource,
                    message = SecurityMessageBundle.errorConflictPassword,
                    parameters = arrayOf(constraintMessage!!)
                ),
                httpStatus = HttpStatus.BAD_REQUEST
            )
        }
        return super.beforeUpdate(properties, entity)
    }

    override fun beforeDelete(entity: TekUser): TekUser {
        repository.deleteUserProfilesByUser(entity.id!!)
        if (entity.preference != null) {
            preferencesRepository.delete(entity.preference!!)
        }
        return super.beforeDelete(entity)
    }

    override fun executeDelete(entity: TekUser) {
        super.executeDelete(entity)
        tokenService.invalidateUserTokens(entity.username!!)
    }
}