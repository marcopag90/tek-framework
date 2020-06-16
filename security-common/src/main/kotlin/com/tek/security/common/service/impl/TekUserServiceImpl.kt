package com.tek.security.common.service.impl

import com.querydsl.core.types.Predicate
import com.tek.security.common.form.ChangeEmailForm
import com.tek.security.common.form.ChangePasswordForm
import com.tek.security.common.form.UserCreateForm
import com.tek.security.common.model.QTekUser
import com.tek.security.common.model.TekProfile
import com.tek.security.common.model.TekUser
import com.tek.security.common.repository.TekUserRepository
import com.tek.security.common.service.TekTokenService
import com.tek.security.common.service.TekUserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TekUserServiceImpl(
    private val repository: TekUserRepository,
    private val tokenService: TekTokenService
) : TekUserService {

    override fun create(form: UserCreateForm) {

        TODO("Not yet implemented")
    }

    override fun read(id: Long): TekUser {
        TODO("Not yet implemented")
    }

    override fun list(pageable: Pageable, predicate: Predicate?): Page<TekUser> {
        TODO("Not yet implemented")
    }

    override fun changeEmail(form: ChangeEmailForm): TekUser {
        TODO("Not yet implemented")
    }

    override fun changePassword(form: ChangePasswordForm): TekUser {
        TODO("Not yet implemented")
    }

    override fun changeProfiles(profiles: Set<Long>) {
        TODO("Not yet implemented")
    }

    override fun setEnabled(enabled: Boolean): TekUser {
        TODO("Not yet implemented")
    }

    @Transactional
    override fun removeUserProfileAndInvalidate(profile: TekProfile) {
        repository.findAll(QTekUser.tekUser.profiles.contains(profile)).forEach { user ->
            user.profiles.remove(profile)
            repository.save(user)
            repository.deleteUserProfilesByProfile(profile.id!!)
            tokenService.invalidateUserTokens(user.username!!)
        }
    }
}