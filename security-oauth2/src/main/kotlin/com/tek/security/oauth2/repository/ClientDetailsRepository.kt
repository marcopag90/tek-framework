package com.tek.security.oauth2.repository

import com.tek.security.oauth2.model.ClientDetails
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientDetailsRepository : JpaRepository<ClientDetails, String>