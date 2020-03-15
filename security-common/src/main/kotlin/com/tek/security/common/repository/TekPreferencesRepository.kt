package com.tek.security.common.repository

import com.tek.security.common.model.TekPreference
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TekPreferencesRepository : JpaRepository<TekPreference, Long>,
    QuerydslPredicateExecutor<TekPreference> {

    fun findByUserId(userId: Long): Optional<TekPreference>
}