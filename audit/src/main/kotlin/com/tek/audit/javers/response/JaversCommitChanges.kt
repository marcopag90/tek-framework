package com.tek.audit.javers.response

import org.javers.core.commit.CommitMetadata
import org.javers.core.diff.Change

data class JaversCommitChanges(
    val metadata: CommitMetadata,
    val changes: List<Change>
)