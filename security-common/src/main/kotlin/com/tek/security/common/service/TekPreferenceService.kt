package com.tek.security.common.service

interface TekPreferenceService {

    fun getUserPreferences(userId: Long): MutableMap<String, Any>?

    fun setUserPreferences(userId: Long, preferences: MutableMap<String, Any>): MutableMap<String, Any>
}