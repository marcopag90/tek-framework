package com.tek.core.service

interface TekMailService {

    fun sendSimpleMessage(to: Array<String>, subject: String, text: String)
}