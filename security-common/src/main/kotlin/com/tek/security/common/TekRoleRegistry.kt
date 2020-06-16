package com.tek.security.common

import com.tek.core.util.LoggerDelegate
import com.tek.security.common.model.TekRole
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

@Component
class TekRoleRegistry {

    private val log by LoggerDelegate()

    private val registry =
        ConcurrentHashMap<KClass<*>, RoleEntry>() as MutableMap<KClass<*>, RoleEntry>

    fun getKeys() = registry.keys
    fun getValues() = registry.values

    fun add(kClass: KClass<*>, role: String) {
        val rolePrefix = role.toUpperCase()
        log.debug(
            "Adding to registry -> {{}, {}} ",
            kClass.simpleName,
            rolePrefix
        )
        registry.putIfAbsent(kClass, createRoleEntry(rolePrefix))
    }

    fun get(kClass: KClass<*>): RoleEntry {
        val roleEntry = registry[kClass]
        require(roleEntry != null) {
            "No role found for ${kClass.simpleName}"
        }
        return roleEntry
    }

    fun countRoles(): Int = registry.values.flatMap { entry -> entry.values.map { it } }.size

    fun getRoleCreate(kClass: KClass<*>): String =
        get(kClass).values.single { it.endsWith("_${RoleAction.CREATE}") }

    fun getRoleRead(kClass: KClass<*>): String =
        get(kClass).values.single { it.endsWith("_${RoleAction.READ}") }

    fun getRoleUpdate(kClass: KClass<*>): String =
        get(kClass).values.single { it.endsWith("_${RoleAction.UPDATE}") }

    fun getRoleDelete(kClass: KClass<*>): String =
        get(kClass).values.single { it.endsWith("_${RoleAction.DELETE}") }

    //----------------------------------------------------------------------------------------------
    private enum class RoleAction { CREATE, READ, UPDATE, DELETE }

    data class RoleEntry(
        val name: String,
        val values: Set<String>
    )

    private fun createRoleEntry(rolePrefix: String): RoleEntry {
        return RoleEntry(
            rolePrefix,
            setOf(
                "${rolePrefix}_${RoleAction.CREATE}",
                "${rolePrefix}_${RoleAction.READ}",
                "${rolePrefix}_${RoleAction.UPDATE}",
                "${rolePrefix}_${RoleAction.DELETE}"
            )
        )
    }
}