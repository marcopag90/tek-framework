package com.tek.core.controller

import com.querydsl.core.types.Predicate
import com.tek.core.TekResponseEntity
import com.tek.core.TekPageResponse
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity

interface ICrudController<Entity, ID, DTO> {

    fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<TekPageResponse<Entity>>

    fun read(id: ID): ResponseEntity<TekResponseEntity<Entity>>

    fun update(properties: Map<String, Any?>, id: ID): ResponseEntity<TekResponseEntity<Entity>>

    fun update(dto: DTO, id: ID): ResponseEntity<TekResponseEntity<Entity>>

    fun delete(id: ID): ResponseEntity<TekResponseEntity<ID>>
}