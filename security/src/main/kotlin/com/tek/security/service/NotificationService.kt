package com.tek.security.service

import com.tek.security.form.ContactForm
import com.tek.security.model.Notification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface NotificationService {

    fun saveContactUsNotification(contactForm: ContactForm): String

    fun listNotificationsByPrivilege(pageable: Pageable): Page<Notification>
}