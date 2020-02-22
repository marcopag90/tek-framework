package com.tek.security.repository

import com.tek.core.repository.TekRepository
import com.tek.security.model.business.Preferences
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PreferencesRepository : TekRepository<Preferences, Long> {

    fun findByUserId(userId: Long): Optional<Preferences>
}