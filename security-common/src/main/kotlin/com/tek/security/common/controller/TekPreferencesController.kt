package com.tek.security.common.controller

import com.tek.core.TekBaseResponse
import com.tek.core.util.LoggerDelegate
import com.tek.security.common.PREFERENCES_PATH
import com.tek.security.common.TekUserDetails
import com.tek.security.common.service.TekPreferenceService
import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@Suppress("unused")
@Api(tags = ["Preferences"])
@RestController
@RequestMapping(path = [PREFERENCES_PATH], produces = [MediaType.APPLICATION_JSON_VALUE])
class TekPreferencesController(
    private val tekPreferenceService: TekPreferenceService
) {

    private val log by LoggerDelegate()

    @GetMapping
    fun getPreferences(): ResponseEntity<TekBaseResponse> =
        ResponseEntity.ok(
            TekBaseResponse(
                HttpStatus.OK,
                tekPreferenceService.getUserPreferences(getPrincipal().id!!)
            )
        )

    @PostMapping
    fun setPreferences(@RequestBody preferences: Map<String, Any>): ResponseEntity<TekBaseResponse> =
        ResponseEntity.ok(
            TekBaseResponse(
                HttpStatus.OK,
                tekPreferenceService.setUserPreferences(
                    getPrincipal().id!!,
                    preferences.toMutableMap()
                )
            )
        )

    private fun getPrincipal() =
        SecurityContextHolder.getContext().authentication.principal as TekUserDetails
}