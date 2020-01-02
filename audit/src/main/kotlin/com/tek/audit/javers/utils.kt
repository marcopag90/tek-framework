package com.tek.audit.javers

import org.javers.core.metamodel.`object`.SnapshotType
import org.javers.repository.jql.QueryBuilder
import java.time.LocalDate
import java.time.LocalTime

/**
 * Extension function to include commit with [SnapshotType.INITIAL]
 */
fun QueryBuilder.setInsertedSnapshot() {
    this.withSnapshotType(SnapshotType.INITIAL)
    this.withNewObjectChanges(true)
}

/**
 * Extension function to include commit with [SnapshotType.UPDATE]
 */
fun QueryBuilder.setUpdatedSnapshot() {
    this.withSnapshotType(SnapshotType.UPDATE)
    this.withNewObjectChanges(false)
}

/**
 * Extension function to include commit with [SnapshotType.TERMINAL]
 */
fun QueryBuilder.setDeletedSnapshot() {
    this.withSnapshotType(SnapshotType.TERMINAL)
}

/**
 * Extension function to set time limit to 23:59:59 (Javers default sets 00:00:00, making upper date bound useless)
 */
fun QueryBuilder.toMax(date: LocalDate): QueryBuilder = this.to(date.atTime(LocalTime.MAX))