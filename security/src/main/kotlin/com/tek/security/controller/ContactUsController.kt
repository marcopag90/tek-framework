package com.tek.security.controller

import com.tek.core.TekBaseResponse
import com.tek.core.util.LoggerDelegate
import com.tek.security.SecurityPattern.CONTACT_PATH
import com.tek.security.form.ContactForm
import com.tek.security.service.ContactUsService
import com.tek.security.service.NotificationService
import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@Suppress("UNUSED")
@Api(tags = ["Contact Us"])
@RestController
@RequestMapping(path = [CONTACT_PATH], produces = [MediaType.APPLICATION_JSON_VALUE])
class ContactUsController(
    private val contactUsService: ContactUsService
) {

    private val log by LoggerDelegate()

    @PostMapping
    fun contactUs(@Valid @RequestBody contactForm: ContactForm): ResponseEntity<TekBaseResponse> {
        log.debug("Executing [POST] method")
        return ResponseEntity.ok(
            TekBaseResponse(HttpStatus.OK, contactUsService.sendContactUsNotification(contactForm))
        )
    }

}