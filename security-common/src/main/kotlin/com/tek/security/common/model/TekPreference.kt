package com.tek.security.common.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.tek.core.converter.HashMapConverter
import javax.persistence.*

@Entity
@Table(name = "preferences")
class TekPreference {

    @Id
    @Column(name = "user_id")
    var userId: Long? = null

    @Column(name = "json_preferences", length = 1024)
    @Convert(converter = HashMapConverter::class)
    var jsonPreferences: MutableMap<String, Any> = mutableMapOf()

    @OneToOne
    @MapsId
    @JsonBackReference
    var user: TekUser? = null
}