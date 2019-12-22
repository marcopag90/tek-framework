package it.jbot.security.controller

import it.jbot.core.JBotBaseResponse
import it.jbot.core.controller.JBotCrudController
import it.jbot.security.model.Privilege
import it.jbot.security.service.impl.PrivilegeCrudService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/privilege")
class PrivilegeController(
    service: PrivilegeCrudService
) : JBotCrudController<Privilege, Long, PrivilegeCrudService>(service) {

    @GetMapping
    fun read(@RequestParam("name") name: String): ResponseEntity<JBotBaseResponse> =
        crudService.read(name)

}