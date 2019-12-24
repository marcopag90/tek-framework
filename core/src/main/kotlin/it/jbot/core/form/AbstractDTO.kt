package it.jbot.core.form

import java.io.Serializable
import javax.persistence.MappedSuperclass

/**
 * Dto to be extended by classes to implement custom validation objects from client
 */
@MappedSuperclass
abstract class AbstractDTO : Serializable {
}