package com.tek.security.common.form

import java.io.Serializable
import javax.persistence.MappedSuperclass

/**
 * Form to be extended by classes to implement custom user registration
 */
abstract class AbstractRegisterForm : Serializable {
    abstract val username: String
    abstract val password: String
    abstract val email: String?
}