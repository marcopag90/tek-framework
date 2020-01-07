package com.tek.security.service

interface TekMailService {

    fun sendSimpleMessage(to: Array<String>, subject: String, text: String)
}