package com.tek.security.common.service.impl

import com.tek.security.common.model.TekPreference
import com.tek.security.common.repository.TekPreferencesRepository
import com.tek.security.common.repository.TekUserRepository
import com.tek.security.common.service.TekPreferenceService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Suppress("unused")
@Service
class TekPreferenceServiceImpl(
    private val userRepository: TekUserRepository,
    private val tekPreferencesRepository: TekPreferencesRepository
) : TekPreferenceService {

    override fun getUserPreferences(userId: Long): MutableMap<String, Any>? =
        userRepository.findPreferencesById(userId)?.preference?.jsonPreferences

    @Transactional
    override fun setUserPreferences(
        userId: Long,
        preferences: MutableMap<String, Any>
    ): MutableMap<String, Any> {

        val userPreferences = tekPreferencesRepository.findByUserId(userId)

        return if (userPreferences.isPresent) {
            for (p in preferences) {
                userPreferences.get().jsonPreferences[p.key] = p.value
            }
            tekPreferencesRepository.save(userPreferences.get()).jsonPreferences
        } else {
            tekPreferencesRepository.save(TekPreference().apply {
                this.user = userRepository.findById(userId).get()
                this.jsonPreferences = preferences
            }).jsonPreferences
        }

    }
}