package com.tek.security.service.impl

import com.tek.security.model.business.Preferences
import com.tek.security.repository.PreferencesRepository
import com.tek.security.repository.TekUserRepository
import com.tek.security.service.PreferenceService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Suppress("UNUSED")
@Service
class PreferenceServiceImpl(
    private val userRepository: TekUserRepository,
    private val preferencesRepository: PreferencesRepository
) : PreferenceService {

    override fun getUserPreferences(userId: Long): MutableMap<String, Any>? =
        userRepository.findPreferencesById(userId)?.preferences?.jsonPreferences

    @Transactional
    override fun setUserPreferences(
        userId: Long,
        preferences: MutableMap<String, Any>
    ): MutableMap<String, Any> {

        val userPreferences = preferencesRepository.findByUserId(userId)

        return if (userPreferences.isPresent) {
            for (p in preferences) {
                userPreferences.get().jsonPreferences[p.key] = p.value
            }
            preferencesRepository.save(userPreferences.get()).jsonPreferences
        } else {
            preferencesRepository.save(Preferences().apply {
                this.user = userRepository.findById(userId).get()
                this.jsonPreferences = preferences
            }).jsonPreferences
        }

    }
}