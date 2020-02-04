package com.tek.security.data.runner

import com.tek.core.util.LoggerDelegate
import com.tek.security.repository.PrivilegeRepository
import com.tek.security.repository.RoleRepository
import com.tek.security.xml.JAXBAuthConfiguration
import com.tek.security.xml.model.Privilege
import com.tek.security.xml.model.Privileges
import com.tek.security.xml.model.Role
import com.tek.security.xml.model.Roles
import org.springframework.boot.CommandLineRunner
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.support.ResourcePatternUtils
import org.springframework.stereotype.Component

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class AuthXmlDataRunner(
    private val jaxbConfiguration: JAXBAuthConfiguration,
    private val resourceLoader: ResourceLoader,
    private val privilegeRepository: PrivilegeRepository,
    private val roleRepository: RoleRepository
) : CommandLineRunner {

    private val log by LoggerDelegate()

    companion object {
        const val rolesXmlPath = "classpath*:auth/*_roles.xml"
        const val privilegesXmlPath = "classpath*:auth/*_privileges.xml"
    }

    override fun run(vararg args: String?) {
        processPrivileges()
        processRoles()
    }

    private fun processPrivileges() {

        log.info("Looking for roles.xml files at path [$privilegesXmlPath] to manage roles...")

        val resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(privilegesXmlPath)
        log.info("Found ${resources.size} resources to load")

        val privilegesList = mutableListOf<Privilege>()
        for (resource in resources) {

            log.info("Loading resource [${resource.filename}]")

            val unmarshaller = jaxbConfiguration.getPrivilegesUnmarshaller()
            val privilegesStream = resource.inputStream
            privilegesStream.use {
                val privileges = unmarshaller.unmarshal(resource.inputStream) as Privileges
                privilegesList.addAll(privileges.privileges)
            }
        }

        log.info("Privileges loaded: [$privilegesList]")
        log.info("Executing database insert...")
    }

    private fun processRoles() {

        log.info("Looking for roles.xml files at path [$rolesXmlPath] to manage roles...")

        val resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(rolesXmlPath)
        log.info("Found ${resources.size} resources to load")

        val rolesList = mutableListOf<Role>()
        for (resource in resources) {

            log.info("Loading resource [${resource.filename}]")

            val unmarshaller = jaxbConfiguration.getRoleUnmarshaller()
            val rolesStream = resource.inputStream
            rolesStream.use {
                val roles = unmarshaller.unmarshal(resource.inputStream) as Roles
                rolesList.addAll(roles.roles)
            }
        }

        log.info("Roles loaded: [$rolesList]")
        log.info("Executing database insert...")

    }
}