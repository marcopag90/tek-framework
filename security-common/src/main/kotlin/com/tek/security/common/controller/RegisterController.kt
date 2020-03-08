package com.tek.security.common.controller

import com.tek.core.TekBaseResponse
import com.tek.security.common.TekSecurityPattern
import com.tek.security.common.form.RegisterForm
import com.tek.security.common.service.TekUserService
import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Suppress("unused")
@Api(tags = ["Register"])
@RestController
@RequestMapping(path = [TekSecurityPattern.REGISTER_PATH], produces = [MediaType.APPLICATION_JSON_VALUE])
class RegisterController(
    private val tekUserService: TekUserService
) {

    @PostMapping
    fun register(@Valid @RequestBody registerForm: RegisterForm): ResponseEntity<TekBaseResponse> {
        return ResponseEntity(
            TekBaseResponse(HttpStatus.OK, tekUserService.register(registerForm)),
            HttpStatus.OK
        )
    }
}