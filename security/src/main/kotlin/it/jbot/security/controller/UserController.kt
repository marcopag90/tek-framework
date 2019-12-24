package it.jbot.security.controller

import com.querydsl.core.types.Predicate
import it.jbot.core.JBotEntityResponse
import it.jbot.core.JBotPageResponse
import it.jbot.security.form.UserForm
import it.jbot.security.model.JBotUser
import it.jbot.security.model.enums.PrivilegeName
import it.jbot.security.service.impl.UserCrudService
import it.jbot.security.util.hasPrivilege
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
class UserController(
    service: UserCrudService
) : JBotAuthorizedCrudController<JBotUser, Long, UserCrudService, UserForm>(service) {

    override fun list(pageable: Pageable, @QuerydslPredicate predicate: Predicate?): ResponseEntity<JBotPageResponse<JBotUser>> {
        return super.list(pageable, predicate)
    }

    override fun createAuthorized(): Boolean = hasPrivilege(PrivilegeName.USER_CREATE)

    override fun readAuthorized(): Boolean = hasPrivilege(PrivilegeName.USER_READ)

    override fun updateAuthorized(): Boolean = hasPrivilege(PrivilegeName.USER_UPDATE)

    override fun deleteAuthorized(): Boolean = hasPrivilege(PrivilegeName.USER_DELETE)
}

