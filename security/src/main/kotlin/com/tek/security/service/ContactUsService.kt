package com.tek.security.service

import com.tek.security.form.ContactForm

interface ContactUsService {

    fun sendContactUsNotification(contactForm: ContactForm): String

}