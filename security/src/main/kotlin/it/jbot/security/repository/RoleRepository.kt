package it.jbot.security.repository

import it.jbot.security.model.Role
import it.jbot.security.model.enums.RoleName
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, Long>, QuerydslPredicateExecutor<Role> {

    fun findByName(name: RoleName): Role?
}