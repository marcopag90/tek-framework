package com.tek.security.controller

import com.tek.core.TekBaseResponse
import com.tek.core.TekPageResponse
import com.tek.core.swagger.ApiPageable
import com.tek.core.util.LoggerDelegate
import com.tek.security.SecurityPattern.NOTIFICATION_PATH
import com.tek.security.model.Notification
import com.tek.security.model.enums.PrivilegeName
import com.tek.security.service.NotificationService
import com.tek.security.util.hasPrivilege
import io.swagger.annotations.Api
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@Suppress("UNUSED")
@Api(tags = ["Notification"])
@RestController
@RequestMapping(path = [NOTIFICATION_PATH], produces = [MediaType.APPLICATION_JSON_VALUE])
class NotificationController(
    private val notificationService: NotificationService
) {

    private val log by LoggerDelegate()

    val readAuthorized get() = hasPrivilege(PrivilegeName.NOTIFICATION_READ)
    val updateAuthorized get() = hasPrivilege(PrivilegeName.NOTIFICATION_UPDATE)

    @PreAuthorize("this.readAuthorized")
    @GetMapping("/list")
    @ApiPageable
    fun read(@ApiIgnore pageable: Pageable): ResponseEntity<TekPageResponse<Notification>> {
        log.debug("Executing [GET] method")
        return ResponseEntity.ok(
            TekPageResponse(HttpStatus.OK, notificationService.listNotificationsByPrivilege(pageable))
        )
    }

    @PreAuthorize("this.updateAuthorized")
    @PostMapping("/isRead/{id}")
    fun setNotificationRead(@PathVariable("id") id: Long): ResponseEntity<TekBaseResponse> {
        log.debug("Executing [POST] method")
        return ResponseEntity.ok(
            TekBaseResponse(HttpStatus.OK, notificationService.setNotificationRead(id))
        )
    }
}