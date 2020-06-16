package com.tek.security.common.service

import com.tek.security.common.model.TekUser

interface TekRegisterService {

    fun isValidPassword(password: String)

    fun isUsernameAlreadyTaken(username: String)

    fun isEmailAlreadyTaken(email: String)

    fun getUserWithDefaultProfile(username: String, profile: String): TekUser
}