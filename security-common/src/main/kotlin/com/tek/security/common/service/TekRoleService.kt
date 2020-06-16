package com.tek.security.common.service

import com.tek.security.common.model.TekRole

interface TekRoleService {

    fun checkIfExists(role: TekRole) : Boolean
}