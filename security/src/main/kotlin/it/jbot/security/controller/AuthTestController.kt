package it.jbot.security.controller

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import org.springframework.security.core.context.SecurityContextHolder

@RestController
@RequestMapping("/auth")
class AuthTestController {
    
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun authAdmin(): String {
        return "success!"
    }
    
    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun authUser(): String {
        return "success!"
    }
    
    @GetMapping("/authentication")
    fun authentication(): Authentication? {
        return SecurityContextHolder.getContext().authentication
    }
    
}