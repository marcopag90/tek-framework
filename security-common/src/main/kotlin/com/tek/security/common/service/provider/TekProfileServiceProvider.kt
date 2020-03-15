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
import org.springframework.stereotype.Service

@Suppress("unused")
@Service
class TekProfileServiceProvider(
    override val repository: TekProfileRepository,
    private val userRepository: TekUserRepository,
    private val tokenService: TekTokenService
) :
    TekCrudEntityService<TekProfile, Long, TekProfileRepository, ProfileForm>(
        QTekProfile.tekProfile.id.stringValue(),
        repository
    ) {

    fun readOneByName(name: String): TekProfile {
        log.debug("Accessing $repository with name:$name")
        repository.findByName(name).orNull()?.let { return it }
            ?: throw TekResourceNotFoundException(
                data = ServiceExceptionData(
                    source = coreMessageSource,
                    message = errorNotFoundResource,
                    parameters = arrayOf(name)
                )
            )
    }

    override fun update(form: ProfileForm, id: Long): TekProfile {
        val profile = super.update(form, id)
        invalidateUserTokens(profile)
        return profile
    }

    override fun update(properties: Map<String, Any?>, id: Long): TekProfile {
        val profile = super.update(properties, id)
        invalidateUserTokens(profile)
        return profile
    }

    override fun delete(id: Long) {
        val profile = repository.findOne(QTekProfile.tekProfile.id.eq(id))
        super.delete(id)
        profile.orNull()?.let {
            userRepository.findAll(QTekUser.tekUser.profiles.contains(it))
                .forEach { user ->
                    user.profiles.remove(it)
                    userRepository.save(user)
                    tokenService.invalidateUserTokens(user.username!!)
                }
        }
    }

    private fun invalidateUserTokens(profile: TekProfile) {
        userRepository.findAll(QTekUser.tekUser.profiles.contains(profile))
            .forEach { tokenService.invalidateUserTokens(it.username!!) }
    }
}