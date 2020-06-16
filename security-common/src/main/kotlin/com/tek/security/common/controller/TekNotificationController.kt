package com.tek.security.common.controller

import com.tek.core.TekBaseResponse
import com.tek.core.TekPageResponse
import com.tek.core.swagger.ApiPageable
import com.tek.core.util.LoggerDelegate
import com.tek.security.common.NOTIFICATION_PATH
import com.tek.security.common.TekRoleRegistry
import com.tek.security.common.hasRole
import com.tek.security.common.model.TekNotification
import com.tek.security.common.service.TekNotificationService
import io.swagger.annotations.Api
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@Suppress("unused")
@Api(tags = ["Notification"])
@RestController
@RequestMapping(path = [NOTIFICATION_PATH], produces = [MediaType.APPLICATION_JSON_VALUE])
class TekNotificationController(
    private val tekNotificationService: TekNotificationService,
    private val tekRoleRegistry: TekRoleRegistry
) {

    private val log by LoggerDelegate()

    val readAuthorized: Boolean
        get() = hasRole(tekRoleRegistry.getRoleRead(TekNotification::class))

    val updateAuthorized: Boolean
        get() = hasRole(tekRoleRegistry.getRoleUpdate(TekNotification::class))

    val deleteAuthorized: Boolean
        get() = hasRole(tekRoleRegistry.getRoleDelete(TekNotification::class))

    @PreAuthorize("this.readAuthorized")
    @GetMapping("/list")
    @ApiPageable
    fun list(
        @ApiIgnore pageable: Pageable,
        @RequestParam("isRead", required = false) isRead: Boolean?
    ): ResponseEntity<TekPageResponse<TekNotification>> {
        log.debug("Executing method: {}", RequestMethod.GET)
        return ResponseEntity.ok(
            TekPageResponse(
                HttpStatus.OK,
                tekNotificationService.listNotificationsByPrivilege(pageable, isRead)
            )
        )
    }

    @PreAuthorize("this.updateAuthorized")
    @PostMapping("/isRead/{id}")
    fun setNotificationRead(@PathVariable("id") id: Long): ResponseEntity<TekBaseResponse> {
        log.debug("Executing method: {}", RequestMethod.POST)
        return ResponseEntity.ok(
            TekBaseResponse(HttpStatus.OK, tekNotificationService.setNotificationRead(id))
        )
    }

    @PreAuthorize("this.deleteAuthorized")
    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<TekBaseResponse> {
        log.debug("Executing method: {}", RequestMethod.DELETE)
        return ResponseEntity.ok(
            TekBaseResponse(HttpStatus.OK, tekNotificationService.delete(id))
        )
    }
}