package com.tek.security.xml.model

import javax.xml.bind.annotation.*

@XmlRootElement(name = "roles") //root access
@XmlAccessorType(XmlAccessType.FIELD)
data class Roles(

    @field:XmlElement(name = "role") //required else empty list
    val roles: MutableList<Role> = mutableListOf()
)

@XmlRootElement(name = "role") //root access
@XmlAccessorType(XmlAccessType.FIELD)
data class Role(

    @field:XmlAttribute(name = "name") //to allow a custom variable name
    val name: String?,

    @field:XmlElement(name = "description")
    val description: String?,

    @field:XmlElement(name = "privileges")
    val privileges: Privileges?
) {
    constructor() : this(name = null, description = null, privileges = null)

    override fun equals(other: Any?): Boolean {
        return (other is Role) && other.name == name
    }

    /*
     * Avoid XML Role duplicates
     */
    override fun hashCode(): Int {
        val result = 17
        return 31 * result + name.hashCode()
    }
}