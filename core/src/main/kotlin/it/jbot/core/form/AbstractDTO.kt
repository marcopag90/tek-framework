package it.jbot.core.form

import java.io.Serializable
import javax.persistence.MappedSuperclass

/**
 * Dto to be extended by classes to implement custom validation objects
 */
@MappedSuperclass
abstract class AbstractDTO : Serializable {
}