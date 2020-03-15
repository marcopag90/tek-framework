package com.tek.core.service

import com.querydsl.core.types.Predicate
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * Interface for all _CRUD_ operations over a given [E].
 *
 * Each implementation can provide read/write access to an [E] data in different ways:
 * 1) Spring Repository Data Interface
 * 2) Web Service
 * 3) Cloud Service
 * 4) etc...
 */
interface ICrudEntityService<E, ID, DTO> {

    fun list(pageable: Pageable, predicate: Predicate?): Page<E>

    fun readOne(id: ID): E

    fun update(properties: Map<String, Any?>, id: ID): E

    fun update(form: DTO, id: ID): E

    fun delete(id: ID)
}