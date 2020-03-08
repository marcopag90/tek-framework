package com.tek.security.common.repository

import com.tek.core.repository.TekRepository
import com.tek.security.common.model.TekPreference
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TekPreferencesRepository : TekRepository<TekPreference, Long> {

    fun findByUserId(userId: Long): Optional<TekPreference>
}