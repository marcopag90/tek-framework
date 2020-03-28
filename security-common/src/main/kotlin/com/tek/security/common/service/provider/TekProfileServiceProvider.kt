package com.tek.security.common.service.provider

import com.tek.core.exception.ServiceExceptionData
import com.tek.core.exception.TekResourceNotFoundException
import com.tek.core.i18n.CoreMessageSource.Companion.errorNotFoundResource
import com.tek.core.service.TekCrudEntityService
import com.tek.core.util.orNull
import com.tek.security.common.form.ProfileForm
import com.tek.security.common.model.QTekProfile
import com.tek.security.common.model.QTekUser
import com.tek.security.common.model.TekProfile
import com.tek.security.common.repository.TekProfileRepository
import com.tek.security.common.repository.TekUserRepository
import com.tek.security.common.service.TekTokenService
import com.tek.security.common.service.TekUserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TekProfileServiceProvider(
    override val repository: TekProfileRepository,
    private val userRepository: TekUserRepository,
    private val userService: TekUserService,
    private val tokenService: TekTokenService
) : TekCrudEntityService<TekProfile, Long, TekProfileRepository, ProfileForm>(
    QTekProfile.tekProfile.id.stringValue(),
    repository
) {

    fun readOneByName(name: String): TekProfile {
        log.debug("Accessing {} with name: {}", repository, name)
        repository.findByName(name).orNull()?.let { return it }
            ?: throw TekResourceNotFoundException(
                data = ServiceExceptionData(
                    source = coreMessageSource,
                    message = errorNotFoundResource,
                    parameters = arrayOf(name)
                )
            )
    }

    @Transactional
    override fun update(form: ProfileForm, id: Long): TekProfile {
        val profile = super.update(form, id)
        invalidateUserTokens(profile)
        return profile
    }

    @Transactional
    override fun update(properties: Map<String, Any?>, id: Long): TekProfile {
        val profile = super.update(properties, id)
        invalidateUserTokens(profile)
        return profile
    }

    @Transactional
    override fun delete(id: Long) {
        log.debug("Accessing {} with id: {}", repository, id)
        repository.findOne(QTekProfile.tekProfile.id.eq(id)).orNull()?.let { profile ->
            repository.deleteProfileRolesByProfile(profile.id!!)
            userService.removeUserProfileAndInvalidate(profile)
            repository.delete(profile)
        } ?: throw TekResourceNotFoundException(
            data = ServiceExceptionData(
                source = coreMessageSource,
                message = errorNotFoundResource,
                parameters = arrayOf(id.toString())
            )
        )
    }

    @Transactional
    private fun invalidateUserTokens(profile: TekProfile) {
        userRepository.findAll(QTekUser.tekUser.profiles.contains(profile))
            .forEach { tokenService.invalidateUserTokens(it.username!!) }
    }
}