package com.tek.core.service

import com.querydsl.core.types.Predicate
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity

/**
 * Interface for all _CRUD_ operations over a given [E].
 *
 * Each implementation can provide read/write access to an [E] data in different ways:
 * 1) Spring Repository Data Interface
 * 2) Web Service
 * 3) Cloud Service
 * 4) etc...
 */
interface ICrudService<E, ID, DTO> {

    fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<TekPageResponse<E>>

    fun update(properties: Map<String, Any?>, id: ID): ResponseEntity<TekResponseEntity<E>>

    fun update(dto: DTO, id: ID): ResponseEntity<TekResponseEntity<E>>
}