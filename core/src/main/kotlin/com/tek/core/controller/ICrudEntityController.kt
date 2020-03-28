package com.tek.core.controller

import com.querydsl.core.types.Predicate
import com.tek.core.TekBaseResponse
import com.tek.core.TekPageResponse
import com.tek.core.TekResponseEntity
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity

interface ICrudEntityController<E, ID, DTO> {

    fun list(pageable: Pageable, predicate: Predicate?): ResponseEntity<TekPageResponse<E>>

    fun read(id: ID): ResponseEntity<TekResponseEntity<E>>

    fun update(properties: Map<String, Any?>, id: ID): ResponseEntity<TekResponseEntity<E>>

    fun update(form: DTO, id: ID): ResponseEntity<TekResponseEntity<E>>

    fun delete(id: ID): ResponseEntity<TekBaseResponse>
}