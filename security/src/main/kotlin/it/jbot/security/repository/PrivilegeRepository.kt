package it.jbot.security.repository

import it.jbot.security.model.Privilege
import it.jbot.security.model.enums.PrivilegeName
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository

@Repository
interface PrivilegeRepository : JpaRepository<Privilege, Long>, QuerydslPredicateExecutor<Privilege>  {

    fun findByName(name: PrivilegeName): Privilege?

}