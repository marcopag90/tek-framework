package com.tek.security.common.crud

import com.tek.core.service.TekReadOnlyCrudService
import com.tek.security.common.model.QTekRole
import com.tek.security.common.model.TekRole
import com.tek.security.common.repository.TekRoleRepository
import org.springframework.stereotype.Service

@Service
class TekRoleCrudService : TekReadOnlyCrudService<TekRole, Long, TekRoleRepository>(
    QTekRole.tekRole.id.stringValue(),
    TekRole::class
)