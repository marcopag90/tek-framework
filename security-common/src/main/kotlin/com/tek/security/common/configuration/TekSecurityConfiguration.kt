package com.tek.security.common.configuration

import com.tek.core.TEK_CORE_CONFIGURATION
import com.tek.core.TEK_PACKAGES_TO_SCAN
import com.tek.core.TekModuleConfiguration
import com.tek.core.configuration.TekCoreConfiguration
import com.tek.core.util.LoggerDelegate
import com.tek.core.util.kClass
import com.tek.security.common.RolePrefix
import com.tek.security.common.TEK_SECURITY_CONFIGURATION
import com.tek.security.common.TekRoleRegistry
import com.tek.security.common.model.TekRole
import com.tek.security.common.repository.TekRoleRepository
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import javax.naming.ConfigurationException

@Configuration(TEK_SECURITY_CONFIGURATION)
@DependsOn(TEK_CORE_CONFIGURATION)
@ConditionalOnBean(TekCoreConfiguration::class)
class TekSecurityConfiguration(
    private val roleRepository: TekRoleRepository,
    private val roleRegistry: TekRoleRegistry
) : TekModuleConfiguration<TekSecurityConfiguration>(TekSecurityConfiguration::class.java) {

    companion object {
        private val log by LoggerDelegate()
    }

    @ExperimentalStdlibApi
    override fun checkModuleConfiguration() {
        checkRolePrefixes()
        checkRoles()
    }

    @ExperimentalStdlibApi
    private fun checkRolePrefixes() {
        log.info("Checking entities with {} annotation...", RolePrefix::class.simpleName)
        val entities = Reflections(
            TEK_PACKAGES_TO_SCAN,
            TypeAnnotationsScanner(),
            SubTypesScanner()
        ).getTypesAnnotatedWith(RolePrefix::class.java)

        log.info(
            "Found {} entities with {} annotation", entities.size, RolePrefix::class.simpleName
        )
        if (entities.isEmpty()) {
            log.warn("All crud operations won't be secured, check your domain configuration!")
        } else {
            log.info("Checking {} setup for each entity...", RolePrefix::class.simpleName)
            for (entity in entities) {
                val rolePrefix = entity.annotations.single { it is RolePrefix } as RolePrefix
                log.debug(
                    "{}: {} -> {${rolePrefix::value.name}={}, ${rolePrefix::enabled.name}={}}",
                    entity.simpleName,
                    RolePrefix::class.simpleName,
                    rolePrefix.value,
                    rolePrefix.enabled
                )
                if (rolePrefix.enabled) {
                    if (rolePrefix.value.isEmpty()) {
                        log.error(
                            "{} with {} wrong configuration: {} property must not be empty when {} is {}}!",
                            entity.simpleName,
                            RolePrefix::class.simpleName,
                            rolePrefix::value.name,
                            rolePrefix::enabled.name,
                            rolePrefix.enabled
                        )
                        throw ConfigurationException("${entity.name} has a wrong configuration!")
                    } else {
                        roleRegistry.add(entity.kotlin, rolePrefix.value)
                    }
                }
            }
            log.info("{} configuration success!", RolePrefix::class.simpleName)
        }
    }

    private fun checkRoles() {
        if (roleRepository.count() == 0L) {
            log.warn(
                "No {} found on database. Performing all required Sql-INSERT.",
                TekRole::class.simpleName
            )
            val roles = roleRegistry.getValues().flatMap { entry ->
                entry.values.map { value -> TekRole(value) }
            }
            roleRepository.saveAll(roles)
        } else {
            roleRegistry.getValues().forEach { entry ->
                entry.values.forEach { value ->
                    if (!roleRepository.existsByName(value)) {
                        log.warn(
                            "{} with value [{}] not found in repository. Seeding database...",
                            TekRole::class.simpleName,
                            value
                        )
                        roleRepository.save(TekRole(value))
                    }
                }
            }
        }
        log.info("Checking [{}] database status", TekRole::class.simpleName)
        val registryCount = roleRegistry.countRoles()
        val repoRolesCount = roleRepository.count().toInt()
        if (registryCount == repoRolesCount) {
            log.info("All [{}] successfully configured!", TekRole::class.simpleName)
        } else {
            //TODO provide more fine gradient information about what role is missing from repo/registry
            log.error(
                "Found [{}] in registry and [{}] in repository. Configuration error!",
                registryCount,
                repoRolesCount
            )
            throw ConfigurationException(
                "Something went wrong! Check your domain configuration!"
            )
        }
    }
}