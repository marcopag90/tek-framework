package com.tek.service

import com.tek.security.common.TekRegisterProvider
import com.tek.security.common.form.AbstractRegisterForm
import com.tek.security.common.model.TekUser
import com.tek.security.common.service.TekAuthService
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AppRegisterService(
    private val authService: TekAuthService
) : TekRegisterProvider(authService) {

    override fun register(form: AbstractRegisterForm) {
        userRepository.save(
            TekUser().apply {
                username = form.username
                password = authService.passwordEncoder().encode(form.password)
                email = form.email
                enabled = true
                pwdExpireAt =
                    LocalDate.now().plusDays(authSecurityProperties.passwordExpiration!!.toDays())
            })
    }
}