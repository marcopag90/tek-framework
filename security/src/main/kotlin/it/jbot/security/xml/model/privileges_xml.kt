package it.jbot.security.xml.model

import javax.xml.bind.annotation.*

@XmlRootElement(name = "privileges")
@XmlAccessorType(XmlAccessType.FIELD)
data class Privileges(

    @field:XmlElement(name = "privilege")
    val privileges: MutableList<Privilege> = mutableListOf()
)

@XmlRootElement(name = "privilege")
@XmlAccessorType(XmlAccessType.FIELD)
data class Privilege(

    @field:XmlAttribute(name = "type")
    val type: String?,

    @field:XmlAttribute(name = "name")
    val name: String?,

    @field:XmlElement(name = "description")
    val description: String?
) {
    constructor() : this(type = null, name = null, description = null)

    override fun equals(other: Any?): Boolean {
        return (other is Privilege) && other.name == name
    }

    /*
     * Avoid XML Privilege duplicates
     */
    override fun hashCode(): Int {
        val result = 17
        return 31 * result + name.hashCode()
    }
}