package it.jbot.security.model

import com.fasterxml.jackson.annotation.JsonProperty
import it.jbot.security.model.enums.PrivilegeName
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.NaturalId
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "privilege")
data class Privilege(
    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 50) val name: PrivilegeName
) : Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(mappedBy = "privileges")
    var roles: MutableSet<Role> = mutableSetOf()
}