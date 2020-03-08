package com.tek.core.form

import java.io.Serializable
import javax.persistence.MappedSuperclass

/**
 * Form to be extended by classes to implement custom validation objects from client
 */
@MappedSuperclass
abstract class AbstractForm : Serializable