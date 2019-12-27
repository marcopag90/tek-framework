package it.jbot.security.controller

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotResponseEntity
import it.jbot.core.JBotPageResponse
import it.jbot.core.controller.JBotCrudController
import it.jbot.core.form.AbstractDTO
import it.jbot.core.service.ICrudService
import it.jbot.core.swagger.ApiPageable
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore
import javax.validation.Valid

/**
 * Extension of [JBotCrudController] to provide security at method access level
 */
abstract class JBotAuthorizedCrudController<E, ID, S : ICrudService<E, ID, DTO>, DTO : AbstractDTO>(
    crudService: S
) : JBotCrudController<E, ID, S, DTO>(crudService) {

    @PreAuthorize("this.readAuthorized()")
    @GetMapping("/list")
    @ApiPageable
    override fun list(@ApiIgnore pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageResponse<E>> {
        return super.list(pageable, predicate)
    }

    @PreAuthorize("this.updateAuthorized()")
    @PatchMapping("/update/{id}")
    override fun update(@RequestBody properties: Map<String, Any?>, @PathVariable("id") id: ID): ResponseEntity<JBotResponseEntity<E>> {
        return super.update(properties, id)
    }

    @PreAuthorize("this.updateAuthorized()")
    @PutMapping("/update/{id}")
    override fun update(@RequestBody @Valid dto: DTO, @PathVariable("id") id: ID): ResponseEntity<JBotResponseEntity<E>> {
        return super.update(dto, id)
    }

    /**
     * Check if the user in session has a [org.springframework.security.core.GrantedAuthority] for the _CREATE_ method
     */
    abstract fun createAuthorized(): Boolean

    /**
     * Check if the user in session has a [org.springframework.security.core.GrantedAuthority] for the _READ_ method
     */
    abstract fun readAuthorized(): Boolean

    /**
     * Check if the user in session has a [org.springframework.security.core.GrantedAuthority] for the _UPDATE_ method
     */
    abstract fun updateAuthorized(): Boolean

    /**
     * Check if the user in session has a [org.springframework.security.core.GrantedAuthority] for the _DELETE_ method
     */
    abstract fun deleteAuthorized(): Boolean

}