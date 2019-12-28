package com.tek.security.controller

import com.tek.core.TekBaseResponse
import com.tek.security.SecurityPattern
import com.tek.security.form.auth.RegisterForm
import com.tek.security.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(SecurityPattern.REGISTER_PATTERN)
class AuthController(
    private val userService: UserService
) {

    @PostMapping
    fun register(@Valid @RequestBody registerForm: RegisterForm): ResponseEntity<TekBaseResponse> {
        return ResponseEntity(
            TekBaseResponse(HttpStatus.OK, userService.register(registerForm)),
            HttpStatus.OK
        )
    }
}