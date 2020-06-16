package com.tek.security.common.crud

import com.tek.core.service.TekWritableCrudService
import com.tek.core.util.orNull
import com.tek.security.common.SECURITY_VALIDATOR
import com.tek.security.common.form.ProfileCreateForm
import com.tek.security.common.form.ProfileUpdateForm
import com.tek.security.common.model.QTekProfile
import com.tek.security.common.model.QTekUser
import com.tek.security.common.model.TekProfile
import com.tek.security.common.repository.TekProfileRepository
import com.tek.security.common.repository.TekUserRepository
import com.tek.security.common.service.TekRoleService
import com.tek.security.common.service.TekTokenService
import com.tek.security.common.service.TekUserService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import javax.validation.Validator

@Service
class TekProfileCrudService(
    @Qualifier(SECURITY_VALIDATOR) override val validator: Validator,
    val userService: TekUserService,
    val tokenService: TekTokenService,
    val tekRoleService: TekRoleService,
    val userRepository: TekUserRepository
) : TekWritableCrudService<TekProfile, Long, TekProfileRepository, ProfileCreateForm, ProfileUpdateForm>(
    QTekProfile.tekProfile.id.stringValue(),
    TekProfile::class,
    validator
) {

    override fun new(form: ProfileCreateForm): TekProfile {
        form.roles.forEach { role -> tekRoleService.checkIfExists(role) }
        return super.new(form)
    }

    override fun beforeUpdate(form: ProfileUpdateForm, entity: TekProfile): TekProfile {
        form.roles.forEach { role -> tekRoleService.checkIfExists(role) }
        return super.beforeUpdate(form, entity)
    }

    override fun afterUpdate(entity: TekProfile): TekProfile {
        val users = userRepository.findAll(QTekUser.tekUser.profiles.contains(entity))
        for (user in users) {
            tokenService.invalidateUserTokens(user.username!!)
        }
        return super.afterUpdate(entity)
    }

    override fun beforeDelete(entity: TekProfile): TekProfile {
        repository.findOne(QTekProfile.tekProfile.id.eq(entity.id)).orNull()?.let { profile ->
            repository.deleteProfileRolesByProfile(profile.id!!)
            userService.removeUserProfileAndInvalidate(profile)
        }
        return super.beforeDelete(entity)
    }
}