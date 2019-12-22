package it.jbot.security.controller

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotEntityResponse
import it.jbot.core.JBotPageResponse
import it.jbot.core.controller.JBotCrudController
import it.jbot.core.service.ICrudService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping

/**
 * Extension of [JBotCrudController] to provide security at method access level
 */
abstract class JBotAuthorizedCrudController<Entity, Id, CrudService : ICrudService<Entity, Id>>(
    crudService: CrudService
) : JBotCrudController<Entity, Id, CrudService>(crudService) {

    @PreAuthorize("this.readAuthorized()")
    override fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<JBotPageResponse<Entity>> {
        return super.list(pageable, predicate)
    }

    @PreAuthorize("this.updateAuthorized()")
    override fun update(properties: Map<String, Any?>, id: Id): ResponseEntity<JBotEntityResponse<Entity>> {
        return super.update(properties, id)
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