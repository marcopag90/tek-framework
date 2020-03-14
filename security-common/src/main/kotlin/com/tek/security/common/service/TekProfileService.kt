package com.tek.security.common.service

import com.querydsl.core.types.Predicate
import com.tek.security.common.model.TekProfile
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TekProfileService {

    fun list(pageable: Pageable, predicate: Predicate?): Page<TekProfile>

    fun readOne(id: Long): TekProfile

    fun readOneByName(name: String) : TekProfile
}