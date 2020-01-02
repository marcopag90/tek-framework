package com.tek.audit.javers

import org.javers.core.metamodel.`object`.SnapshotType
import org.javers.repository.jql.QueryBuilder
import java.time.LocalDate
import java.time.LocalTime

fun QueryBuilder.setInsertedSnapshot() {
    this.withSnapshotType(SnapshotType.INITIAL)
    this.withNewObjectChanges(true)
}

fun QueryBuilder.setUpdatedSnapshot() {
    this.withSnapshotType(SnapshotType.UPDATE)
    this.withNewObjectChanges(false)
}

fun QueryBuilder.toMax(date: LocalDate): QueryBuilder = this.to(date.atTime(LocalTime.MAX))