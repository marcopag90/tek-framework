package com.tek.security.controller

import com.querydsl.core.types.Predicate
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import com.tek.core.swagger.ApiPageable
import com.tek.core.util.LoggerDelegate
import com.tek.security.model.TekUser
import com.tek.security.model.enums.PrivilegeName
import com.tek.security.service.UserService
import com.tek.security.util.hasPrivilege
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import springfox.documentation.annotations.ApiIgnore

@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {

    private val log by LoggerDelegate()

    fun createAuthorized() = hasPrivilege(PrivilegeName.USER_CREATE)

    fun readAuthorized() = hasPrivilege(PrivilegeName.USER_READ)

    fun updateAuthorized() = hasPrivilege(PrivilegeName.USER_UPDATE)

    fun deleteAuthorized() = hasPrivilege(PrivilegeName.USER_DELETE)

    @PreAuthorize("this.readAuthorized()")
    @GetMapping("/list")
    @ApiPageable
    fun list(@ApiIgnore pageable: Pageable, @QuerydslPredicate predicate: Predicate?): ResponseEntity<TekPageResponse<TekUser>> {
        log.debug("Executing [GET] method")
        return userService.list(pageable, predicate)
    }

    @PreAuthorize("this.readAuthorized()")
    @GetMapping("/read/{id}")
    fun readOne(@PathVariable("id") id: Long): ResponseEntity<TekResponseEntity<TekUser>> {
        log.debug("Executing [GET] method")
        return userService.readOne(id)
    }

    @PreAuthorize("this.deleteAuthorized()")
    @DeleteMapping("/delete/{id}")
    fun delete(@PathVariable("id") id: Long): ResponseEntity<TekResponseEntity<Long>> {
        log.debug("Executing [DELETE] method")
        return userService.delete(id)
    }
}


