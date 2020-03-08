package com.tek.security.common.service

interface TekMailService {

    fun sendSimpleMessage(to: Array<String>, subject: String, text: String)
}