package com.tek.security.service

interface PreferenceService {

    fun getUserPreferences(userId: Long): MutableMap<String, Any>?

    fun setUserPreferences(userId: Long, preferences: MutableMap<String, Any>): MutableMap<String, Any>
}