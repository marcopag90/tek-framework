package com.tek.security.service

import com.querydsl.core.types.Predicate
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import com.tek.security.form.auth.RegisterForm
import com.tek.security.model.TekUser
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity

interface UserService {

    fun register(registerForm: RegisterForm): TekUser

    fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<TekPageResponse<TekUser>>

    fun readOne(id: Long): ResponseEntity<TekResponseEntity<TekUser>>

    fun update(properties: Map<String, Any?>, id: Long): ResponseEntity<TekResponseEntity<TekUser>>

    fun delete(id: Long): ResponseEntity<TekResponseEntity<Long>>

}