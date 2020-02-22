package com.tek.security.model.business

import com.tek.core.converter.HashMapConverter
import com.tek.security.model.auth.TekUser
import javax.persistence.*

@Entity
@Table(name = "preferences")
class Preferences {

    @Id
    @Column(name = "user_id")
    var userId: Long? = null

    @Column(name = "json_preferences", length = 1024)
    @Convert(converter = HashMapConverter::class)
    var jsonPreferences: MutableMap<String, Any> = mutableMapOf()

    @OneToOne
    @MapsId
    var user: TekUser? = null
}