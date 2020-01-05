package com.tek.security.service

import com.querydsl.core.types.Predicate
import com.tek.security.form.auth.RegisterForm
import com.tek.security.model.auth.TekUser
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UserService {

    fun register(registerForm: RegisterForm): TekUser

    fun list(pageable: Pageable, predicate: Predicate?): Page<TekUser>

    fun readOne(id: Long): TekUser

    fun update(properties: Map<String, Any?>, id: Long): TekUser

    fun delete(id: Long): Long
}