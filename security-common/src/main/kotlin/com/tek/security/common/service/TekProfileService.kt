package com.tek.security.common.service

import com.tek.security.common.model.TekProfile

interface TekProfileService {

    fun checkIfExists(profile: TekProfile) : Boolean
}