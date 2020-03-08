package com.tek.core.service

import com.querydsl.core.types.Predicate
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * Interface for all _CRUD_ operations over a given [Entity].
 *
 * Each implementation can provide read/write access to an [Entity] data in different ways:
 * 1) Spring Repository Data Interface
 * 2) Web Service
 * 3) Cloud Service
 * 4) etc...
 */
interface ICrudService<Entity, ID, Form> {

    fun list(pageable: Pageable, predicate: Predicate?): Page<Entity>

    fun readOne(id: ID): Entity

    fun update(properties: Map<String, Any?>, id: ID): Entity

    fun update(form: Form, id: ID): Entity

    fun delete(id: ID): ID
}